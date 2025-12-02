package com.boyouquan.scheduler;

import com.boyouquan.service.BlogService;
import com.boyouquan.service.FriendLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendLinkScheduler {

    private final Logger logger = LoggerFactory.getLogger(FriendLinkScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private FriendLinkService friendLinkService;

    @Scheduled(cron = "0 0 1 ? * SUN")
    public void processBlogs() {
        logger.info("friend link scheduler start!");

        detectFriendLinks();

        logger.info("friend link scheduler end!");
    }

    private void detectFriendLinks() {
        List<Blog> blogs = blogService.listAll();

        for (Blog blog : blogs) {
            try {
                logger.info("friend link detection, blogDomainName: {}", blog.getDomainName());

                friendLinkService.detectFriendLinks(blog, true);
            } catch (Exception e) {
                logger.error("friend link process failed", e);
            }
        }
    }

}

