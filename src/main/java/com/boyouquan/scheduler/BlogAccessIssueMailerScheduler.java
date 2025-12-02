package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogStatus;
import com.boyouquan.model.EmailLog;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.EmailLogService;
import com.boyouquan.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlogAccessIssueMailerScheduler {

    private final Logger logger = LoggerFactory.getLogger(BlogAccessIssueMailerScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private EmailLogService emailLogService;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void processBlogs() {
        logger.info("blog access issue mailer scheduler start!");

        processIssueBlogs();

        logger.info("blog access issue mailer scheduler end!");
    }

    private void processIssueBlogs() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("process for {}", blog.getDomainName());

                BlogStatus blogStatus = blogStatusService.getLatestByBlogDomainName(blog.getDomainName());
                if (null != blogStatus && !BlogStatus.Status.ok.equals(blogStatus.getStatus())) {
                    String unOkInfo = blogStatusService.getUnOkInfo(blog.getDomainName(), blog.getCollectedAt());

                    if (need2SendEmail(blog, blogStatus)) {
                        emailService.sendBlogStatusNotOkNotice(blog, blogStatus.getStatus(), unOkInfo);

                        if (!blog.getAdminEmail().equals(CommonConstants.FAKE_BLOG_ADMIN_EMAIL)
                                && !blog.getAdminEmail().startsWith(CommonConstants.FAKE_BLOG_ADMIN_EMAIL_PREFIX)) {
                            // save email log
                            EmailLog newEmailLog = new EmailLog();
                            newEmailLog.setBlogDomainName(blog.getDomainName());
                            newEmailLog.setEmail(blog.getAdminEmail());
                            newEmailLog.setType(EmailLog.Type.blog_can_not_be_accessed);
                            emailLogService.save(newEmailLog);

                            logger.info("blog can not access notice sent, blog: {}", blog.getDomainName());
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

    private boolean need2SendEmail(Blog blog, BlogStatus blogStatus) {
        long now = System.currentTimeMillis();
        long detectedAt = blogStatus.getDetectedAt().getTime();
        long oneDay = (long) 24 * 60 * 60 * 1000;
        long oneYear = 365 * oneDay;

        boolean need2SendEmail = (now > detectedAt)
                && (now - detectedAt) >= (3 * oneDay)
                && ((now - detectedAt) < oneYear);

        EmailLog emailLog = emailLogService.getLatestByBlogDomainNameAndType(blog.getDomainName(), EmailLog.Type.blog_can_not_be_accessed);

        if (null != emailLog) {
            long sentAt = emailLog.getSentAt().getTime();
            need2SendEmail = (now > sentAt)
                    && ((now - sentAt) > 30 * oneDay);
        }

        return blog.getSelfSubmitted() && need2SendEmail;
    }

}

