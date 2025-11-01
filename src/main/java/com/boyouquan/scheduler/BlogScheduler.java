package com.boyouquan.scheduler;

import com.boyouquan.model.BlogRequest;
import com.boyouquan.service.BlogRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlogScheduler {

    private final Logger logger = LoggerFactory.getLogger(BlogScheduler.class);

    @Autowired
    private BlogRequestService blogRequestService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void processBlogs() {
        logger.info("blog scheduler start!");

        processSubmittedBlog();

        logger.info("blog scheduler end!");
    }

    private void processSubmittedBlog() {
        List<BlogRequest> blogRequests = blogRequestService.listByStatus(BlogRequest.Status.submitted);
        for (BlogRequest blogRequest : blogRequests) {
            try {
                logger.info("process for {}", blogRequest.getRssAddress());
                blogRequestService.processNewRequest(blogRequest.getRssAddress());
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

}

