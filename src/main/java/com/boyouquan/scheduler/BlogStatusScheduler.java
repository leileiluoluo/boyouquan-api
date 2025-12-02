package com.boyouquan.scheduler;

import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlogStatusScheduler {

    private final Logger logger = LoggerFactory.getLogger(BlogStatusScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;

    @Scheduled(cron = "0 0 0/4 * * ?")
    public void processBlogStatuses() {
        logger.info("blog status scheduler start!");

        detectBlogStatuses();

        logger.info("blog status scheduler end!");
    }

    private void detectBlogStatuses() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("process for {}", blog.getDomainName());
                blogStatusService.detectBlogStatus(blog);
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

}

