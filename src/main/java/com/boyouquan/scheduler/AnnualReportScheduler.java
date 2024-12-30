package com.boyouquan.scheduler;

import com.boyouquan.model.Blog;
import com.boyouquan.service.AnnualReportService;
import com.boyouquan.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class AnnualReportScheduler {

    private final Logger logger = LoggerFactory.getLogger(AnnualReportScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private AnnualReportService annualReportService;

    @Scheduled(cron = "0 0 17 30 12 ?")
    public void sendAnnualReports() {
        logger.info("annual report scheduler start!");

        List<Blog> blogs = blogService.listAll();
        logger.info("blog size: {}", blogs.size());

        for (Blog blog : blogs) {
            logger.info("send annual report for {}", blog.getDomainName());
            try {
                annualReportService.sendAnnualReport(blog.getDomainName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        logger.info("annual report scheduler end!");
    }

}
