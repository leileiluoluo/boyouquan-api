package com.boyouquan.scheduler;

import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.DomainNameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class DomainNameInfoScheduler {

    private final Logger logger = LoggerFactory.getLogger(DomainNameInfoScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private DomainNameInfoService domainNameInfoService;

    public void processDomainNameInfos() {
        logger.info("domain name infos scheduler start!");

        detectDomainNameInfos();

        logger.info("domain name infos scheduler end!");
    }

    private void detectDomainNameInfos() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("process for {}", blog.getDomainName());

                TimeUnit.SECONDS.sleep(3);
                domainNameInfoService.refreshDomainNameInfo(blog.getDomainName());
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

}

