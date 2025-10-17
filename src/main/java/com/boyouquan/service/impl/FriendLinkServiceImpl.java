package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.FriendLinkDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogIntimacy;
import com.boyouquan.model.FriendLink;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.FriendLinkService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkDaoMapper friendLinkDaoMapper;
    @Autowired
    private BlogService blogService;

    @Override
    public BlogIntimacy computeBlogIntimacies(String sourceBlogDomainName, String targetBlogDomainName) {
        Map<String, List<String>> graph = buildLinkGraph();
        return bfsShortestPath(graph, sourceBlogDomainName, targetBlogDomainName);
    }

    @Override
    public void detectFriendLinks(Set<String> blogDomainNames, Blog blog) {
        List<String> internalLinks = Collections.emptyList();

        try {
            Document doc = Jsoup.connect(blog.getAddress())
                    .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                    .timeout(10000).get();

            internalLinks = doc.select("a[href]").stream()
                    .map(a -> a.absUrl("href"))
                    .filter(url -> url.startsWith(blog.getAddress()))
                    .filter(url -> !url.endsWith(".xml"))
                    .limit(200)
                    .toList();
        } catch (IOException e) {
            log.error("connect failed, blogDomainName: {}", blog.getDomainName(), e);
        }

        List<String> shortestInternalLinks = internalLinks.stream()
                .sorted(Comparator.comparingInt(String::length))
                .limit(20)
                .toList();

        List<FriendLink> friendLinks = new ArrayList<>();
        for (String internalLink : shortestInternalLinks) {
            try {
                Document page = Jsoup.connect(internalLink)
                        .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                        .timeout(10000).get();
                String title = Optional.of(page.title()).orElse("无标题");

                for (String targetDomainName : blogDomainNames) {
                    if (!blog.getAddress().contains(targetDomainName)
                            && page.outerHtml().contains(targetDomainName)) {
                        if (!blogDomainNames.contains(targetDomainName)) {
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

    private BlogIntimacy bfsShortestPath(Map<String, List<String>> graph, String start, String target) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(List.of(start));
        visited.add(start);

        int maxDepth = 10;
        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String last = path.get(path.size() - 1);
            if (last.equals(target)) return BlogIntimacy.builder().path(path).build();

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
        return BlogIntimacy.builder()
                .path(Collections.emptyList())
                .build();
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

}
