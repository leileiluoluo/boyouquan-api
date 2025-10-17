package com.boyouquan.scheduler;

import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.FriendLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class FriendLinkScheduler {

    private final Logger logger = LoggerFactory.getLogger(FriendLinkScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private FriendLinkService friendLinkService;

    @Scheduled(cron = "0 30 20 ? * FRI")
    public void processBlogs() {
        logger.info("friend link scheduler start!");

        detectFriendLinks();

        logger.info("friend link scheduler end!");
    }

    private void detectFriendLinks() {
        List<Blog> blogs = blogService.listAll();

        Set<String> blogAddresses = blogs.stream()
                .map(Blog::getAddress)
                .collect(Collectors.toSet());

        for (Blog blog : blogs) {
            try {
                friendLinkService.detectFriendLinks(blogAddresses, blog);
            } catch (Exception e) {
                logger.error("friend link process failed", e);
            }
        }
    }

}

