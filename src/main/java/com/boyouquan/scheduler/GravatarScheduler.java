package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.GravatarService;
import com.boyouquan.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class GravatarScheduler {

    private final Logger logger = LoggerFactory.getLogger(GravatarScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private GravatarService gravatarService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshGravatar() {
        logger.info("gravatar scheduler start!");

        refreshBlogGravatarImages();

        logger.info("gravatar scheduler end!");
    }

    private void refreshBlogGravatarImages() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                String adminEmail = blog.getAdminEmail();
                if (!CommonConstants.FAKE_BLOG_ADMIN_EMAIL.equals(adminEmail)) {
                    String md5Email = CommonUtils.md5(adminEmail);
                    logger.info("refresh blog gravatar for: {}", blog.getDomainName());

                    // refresh
                    gravatarService.refreshLocalImage(md5Email, CommonConstants.GRAVATAR_IMAGE_MEDIUM_SIZE);
                    gravatarService.refreshLocalImage(md5Email, CommonConstants.GRAVATAR_IMAGE_LARGE_SIZE);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
