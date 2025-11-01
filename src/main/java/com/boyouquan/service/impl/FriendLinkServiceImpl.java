package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.FriendLinkDaoMapper;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogIntimacySearchHistoryService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.FriendLinkService;
import com.boyouquan.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    private static Map<String, List<String>> STATIC_GRAPH = new HashMap<>();
    private static final Map<String, List<String>> GRAPH_PATH_DATA = new HashMap<>();

    @Autowired
    private FriendLinkDaoMapper friendLinkDaoMapper;
    @Autowired
    private IPControlHelper ipControlHelper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogIntimacySearchHistoryService blogIntimacySearchHistoryService;
    @Autowired
    private WebSocketService webSocketService;

    @Scheduled(cron = "0 0 22 18 * ?")
    public void clearMap() {
        log.info("prepare to clear static graph maps, keys: {}", STATIC_GRAPH.keySet().size());

        STATIC_GRAPH.clear();
        GRAPH_PATH_DATA.clear();

        log.info("static graph maps cleared!");
    }

    @Override
    public BlogIntimacy computeBlogIntimacies(String ip, String sourceBlogDomainName, String targetBlogDomainName) {
        BlogInfo sourceBlog = blogService.getBlogInfoByDomainName(sourceBlogDomainName);
        BlogInfo targetBlog = blogService.getBlogInfoByDomainName(targetBlogDomainName);

        List<String> path = getShortestPath(sourceBlogDomainName, targetBlogDomainName);

        // assemble
        List<FriendLinkInfo> pathDetails = assemblePathDetails(path);

        // send broadcast
        if (!pathDetails.isEmpty()) {
            saveBlogIntimacySearchHistory(ip, sourceBlogDomainName, targetBlogDomainName, pathDetails.size());

            if (!ipControlHelper.alreadyPublishBroadcast(ip, CommonConstants.BROADCAST_TYPE_LINK_GRAPHS)) {
                sendBroadCast(pathDetails);
                ipControlHelper.publishBroadcast(ip, CommonConstants.BROADCAST_TYPE_LINK_GRAPHS);
            }
        }

        return BlogIntimacy.builder()
                .sourceBlog(sourceBlog)
                .targetBlog(targetBlog)
                .path(pathDetails)
                .build();
    }

    @Override
    public MyFriendLinks getMyFriendLinks(String blogDomainName) {
        List<String> directSourceBlogDomainNames = friendLinkDaoMapper.getDirectSourceBlogDomainNames(blogDomainName);
        List<String> directTargetBlogDomainNames = friendLinkDaoMapper.getDirectTargetBlogDomainNames(blogDomainName);

        List<Blog> directSourceBlogs = new ArrayList<>();
        List<Blog> directTargetBlogs = new ArrayList<>();

        for (String domainName : directSourceBlogDomainNames) {
            Blog blog = blogService.getByDomainName(domainName);
            directSourceBlogs.add(blog);
        }

        for (String domainName : directTargetBlogDomainNames) {
            Blog blog = blogService.getByDomainName(domainName);
            directTargetBlogs.add(blog);
        }

        return MyFriendLinks.builder()
                .linksFromMe(directTargetBlogs)
                .linksToMe(directSourceBlogs)
                .build();
    }

    @Override
    public List<BlogShortInfo> listAllSourceBlogs() {
        return friendLinkDaoMapper.listAllSourceBlogs();
    }

    @Override
    public List<BlogShortInfo> listAllTargetBlogs() {
        return friendLinkDaoMapper.listAllTargetBlogs();
    }

    @Override
    public Date getMaxCreatedAt() {
        return friendLinkDaoMapper.getMaxCreatedAt();
    }

    @Override
    public void detectFriendLinks(Blog blog) {
        if (null == blog) {
            log.error("blog not found");
            return;
        }

        List<String> allBlogDomainNames = blogService.listAllDomainNames();

        List<String> internalLinks = Collections.emptyList();

        try {
            Document doc = Jsoup.connect(blog.getAddress())
                    .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                    .timeout(10000).get();

            internalLinks = doc.select("a[href]").stream()
                    .map(a -> a.absUrl("href"))
                    .filter(url -> url.startsWith(blog.getAddress()))
                    .filter(url -> !url.endsWith(".xml"))
                    .filter(url -> !url.endsWith(".txt"))
                    .distinct()
                    .limit(200)
                    .toList();
        } catch (IOException e) {
            log.error("connect failed, blogDomainName: {}", blog.getDomainName(), e);
        }

        List<String> shortestInternalLinks = internalLinks.stream()
                .sorted(Comparator.comparingInt(String::length))
                .limit(20)
                .toList();

        log.info("shortestInternalLinks size: {}", shortestInternalLinks.size());

        List<FriendLink> friendLinks = new ArrayList<>();
        for (String internalLink : shortestInternalLinks) {
            log.info("detection for internalLink: {}", internalLink);
            try {
                Document page = Jsoup.connect(internalLink)
                        .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                        .timeout(10000).get();
                String title = Optional.of(page.title()).orElse("无标题");

                for (String targetDomainName : allBlogDomainNames) {
                    if (!blog.getAddress().contains(targetDomainName)
                            && page.outerHtml().contains(targetDomainName)) {
                        List<String> targetDomainNames = friendLinks.stream()
                                .map(FriendLink::getTargetBlogDomainName)
                                .toList();

                        if (!targetDomainNames.contains(targetDomainName)) {
                            FriendLink link = new FriendLink();
                            link.setSourceBlogDomainName(blog.getDomainName());
                            link.setTargetBlogDomainName(targetDomainName);
                            link.setPageTitle(title);
                            link.setPageUrl(internalLink);

                            friendLinks.add(link);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        // save
        saveOrUpdate(blog.getDomainName(), friendLinks);
    }

    @Override
    public void deleteBySourceBlogDomainName(String domainName) {
        friendLinkDaoMapper.deleteBySourceBlogDomainName(domainName);
    }

    @Override
    public void deleteByTargetBlogDomainName(String domainName) {
        friendLinkDaoMapper.deleteByTargetBlogDomainName(domainName);
    }

    @Override
    public void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks) {
        if (friendLinks.isEmpty()) {
            return;
        }

        log.info("sourceBlogDomainName: {}, friendLinks size: {}", sourceBlogDomainName, friendLinks.size());

        friendLinkDaoMapper.deleteBySourceBlogDomainName(sourceBlogDomainName);

        for (FriendLink link : friendLinks) {
            friendLinkDaoMapper.save(link);
        }
    }

    private List<String> getShortestPath(String start, String target) {
        // cache hit
        String key = start + "#" + target;
        if (GRAPH_PATH_DATA.containsKey(key)) {
            return GRAPH_PATH_DATA.get(key);
        }

        // cache miss
        Map<String, List<String>> graph = getLinkGraph();
        List<String> path = bfsShortestPath(graph, start, target);
        if (!path.isEmpty()) {
            GRAPH_PATH_DATA.put(key, path);
        }
        return path;
    }

    private List<String> bfsShortestPath(Map<String, List<String>> graph, String start, String target) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(List.of(start));
        visited.add(start);

        int maxDepth = 10;
        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String last = path.get(path.size() - 1);
            if (last.equals(target)) return path;

            if (path.size() > maxDepth) continue;
            for (String next : graph.getOrDefault(last, List.of())) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    queue.add(newPath);
                }
            }
        }
        return Collections.emptyList();
    }

    private synchronized Map<String, List<String>> getLinkGraph() {
        if (STATIC_GRAPH.isEmpty()) {
            log.info("static graph is empty, now build it!");
            STATIC_GRAPH = buildLinkGraph();
        }
        return STATIC_GRAPH;
    }

    private Map<String, List<String>> buildLinkGraph() {
        List<FriendLink> links = friendLinkDaoMapper.listAll();
        Map<String, List<String>> graph = new HashMap<>();
        for (FriendLink link : links) {
            graph.computeIfAbsent(
                            link.getSourceBlogDomainName(),
                            k -> new ArrayList<>())
                    .add(link.getTargetBlogDomainName());
        }
        return graph;
    }

    private List<FriendLinkInfo> assemblePathDetails(List<String> path) {
        if (path.isEmpty() || path.size() < 2) {
            return Collections.emptyList();
        }

        List<FriendLinkInfo> friendLinks = new ArrayList<>();
        for (int i = 0, j = 1; j < path.size(); i++, j++) {
            String source = path.get(i);
            String target = path.get(j);

            FriendLink link = friendLinkDaoMapper.getBySourceAndTargetBlogDomainName(source, target);
            if (null != link) {
                FriendLinkInfo linkInfo = new FriendLinkInfo();
                BeanUtils.copyProperties(link, linkInfo);

                BlogInfo sourceBlog = blogService.getBlogInfoByDomainName(source);
                BlogInfo targetBlog = blogService.getBlogInfoByDomainName(target);
                if (null != sourceBlog && null != targetBlog) {
                    linkInfo.setSourceBlog(sourceBlog);
                    linkInfo.setTargetBlog(targetBlog);

                    friendLinks.add(linkInfo);
                }
            }
        }

        return friendLinks;
    }

    private void saveBlogIntimacySearchHistory(String ipAddress, String sourceBlogDomainName, String targetBlogDomainName, int pathLength) {
        BlogIntimacySearchHistory history = new BlogIntimacySearchHistory();
        history.setSourceBlogDomainName(sourceBlogDomainName);
        history.setTargetBlogDomainName(targetBlogDomainName);
        history.setPathLength(pathLength);
        history.setIpAddress(ipAddress);
        blogIntimacySearchHistoryService.save(history);
    }

    private void sendBroadCast(List<FriendLinkInfo> pathDetails) {
        if (!pathDetails.isEmpty()) {
            // websocket broadcast
            BlogInfo sourceBlog = pathDetails.get(0).getSourceBlog();
            BlogInfo targetBlog = pathDetails.get(pathDetails.size() - 1).getTargetBlog();

            WebSocketMessage message = new WebSocketMessage();
            message.setMessage(String.format("刚刚有人探索了「%s」和「%s」的连接系数，快来看看吧！", sourceBlog.getName(), targetBlog.getName()));
            message.setGotoUrl(String.format(CommonConstants.GRAPH_LINKS_ADDRESS, sourceBlog.getDomainName(), targetBlog.getDomainName()));
            webSocketService.broadcast(message);
        }
    }

}
