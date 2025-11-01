package com.boyouquan.scheduler;

import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogLocationService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class BlogLocationScheduler {

    private final Logger logger = LoggerFactory.getLogger(BlogLocationScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private BlogLocationService blogLocationService;

    @Scheduled(cron = "0 0 8 ? * MON")
    public void refreshBlogLocationsJob() {
        logger.info("blog location scheduler start!");

        refreshBlogLocations();

        logger.info("blog location scheduler end!");
    }

    private void refreshBlogLocations() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                TimeUnit.SECONDS.sleep(1);

                String blogDomainName = blog.getDomainName();
                boolean statusOK = blogStatusService.isStatusOkByBlogDomainName(blogDomainName);

                if (statusOK) {
                    logger.info("process for {}", blogDomainName);

                    // refresh location
                    blogLocationService.refreshLocation(blogDomainName);
                }
            } catch (Exception e) {
                logger.error("refresh failed", e);
            }
        }
    }

}

