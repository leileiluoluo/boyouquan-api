package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LatestNewsServiceImpl implements LatestNewsService {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @Override
    public List<LatestNews> getLatestNews() {
        List<LatestNews> news = new ArrayList<>();

        // recent added blogs
        List<LatestNews> recentAddedBlogs = getRecentAddedBlogsNews();
        if (!recentAddedBlogs.isEmpty()) {
            news.addAll(recentAddedBlogs);
        }

        // most accessed blogs
        List<LatestNews> mostAccessedBlogs = getMostAccessedBlogsNews();
        if (!mostAccessedBlogs.isEmpty()) {
            news.addAll(mostAccessedBlogs);
        }

        // most updated blogs
        List<LatestNews> mostUpdatedBlogs = getMostUpdatedBlogsNews();
        if (!mostUpdatedBlogs.isEmpty()) {
            news.addAll(mostUpdatedBlogs);
        }

        // most initiated blogs
        List<LatestNews> mostInitiatedBlogs = getMostInitiatedBlogsNews();
        if (!mostInitiatedBlogs.isEmpty()) {
            news.addAll(mostInitiatedBlogs);
        }

        return news;
    }

    private List<LatestNews> getRecentAddedBlogsNews() {
        final List<LatestNews> latestNews = new ArrayList<>();

        List<Blog> blogs = blogService.listRecentCollected(CommonConstants.BLOG_ADDED_LIMIT_SIZE);
        for (Blog blog : blogs) {
            LatestNews news = new LatestNews();
            String title = String.format(CommonConstants.BLOG_ADDED_WELCOME_PATTERN, blog.getName());
            news.setTitle(title);
            news.setLink(CommonConstants.BLOG_LIST_ADDRESS_SORT_BY_COLLECT_TIME);
            latestNews.add(news);
        }

        return latestNews;
    }

    private List<LatestNews> getMostAccessedBlogsNews() {
        BlogDomainNameAccess blogDomainNameAccess = accessService.getMostAccessedBlogDomainNameInLastMonth();
        if (null == blogDomainNameAccess) {
            return Collections.emptyList();
        }

        Blog blog = blogService.getByDomainName(blogDomainNameAccess.getBlogDomainName());
        if (null == blog) {
            return Collections.emptyList();
        }

        String title = String.format(CommonConstants.MOST_ACCESSED_BLOG_ANNOUNCE_PATTERN, blog.getName(), blogDomainNameAccess.getAccessCount());
        String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, blog.getDomainName());

        LatestNews news = new LatestNews();
        news.setTitle(title);
        news.setLink(link);

        return List.of(news);
    }

    private List<LatestNews> getMostUpdatedBlogsNews() {
        BlogDomainNamePublish blogDomainNamePublish = postService.getMostPublishedInLatestOneMonth();
        if (null == blogDomainNamePublish) {
            return Collections.emptyList();
        }
        Blog blog = blogService.getByDomainName(blogDomainNamePublish.getBlogDomainName());

        String title = String.format(CommonConstants.MOST_UPDATED_BLOG_ANNOUNCE_PATTERN, blog.getName(), blogDomainNamePublish.getPostCount());
        String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, blog.getDomainName());

        LatestNews news = new LatestNews();
        news.setTitle(title);
        news.setLink(link);

        return List.of(news);
    }

    private List<LatestNews> getMostInitiatedBlogsNews() {
        BlogDomainNameInitiated blogDomainNameInitiated = planetShuttleService.getMostInitiatedBlogDomainNameInLastMonth();
        if (null == blogDomainNameInitiated) {
            return Collections.emptyList();
        }

        Blog blog = blogService.getByDomainName(blogDomainNameInitiated.getBlogDomainName());

        String title = String.format(CommonConstants.MOST_INITIATED_BLOG_ANNOUNCE_PATTERN, blog.getName(), blogDomainNameInitiated.getInitiatedCount());
        String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, blog.getDomainName());

        LatestNews news = new LatestNews();
        news.setTitle(title);
        news.setLink(link);

        return List.of(news);
    }

}
