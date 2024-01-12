package com.boyouquan.scheduler;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogStatus;
import com.boyouquan.model.EmailLog;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.EmailLogService;
import com.boyouquan.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
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

    @Scheduled(cron = "0 0/30 * * * ?")
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
                    String unOkInfo = blogStatusService.getUnOkInfo(blog.getDomainName());

                    if (need2SendEmail(blog, blogStatus)) {
                        emailService.sendBlogStatusNotOkNotice(blog, blogStatus.getStatus(), unOkInfo);

                        // save email log
                        EmailLog newEmailLog = new EmailLog();
                        newEmailLog.setBlogDomainName(blog.getDomainName());
                        newEmailLog.setEmail(blog.getAdminEmail());
                        newEmailLog.setType(EmailLog.Type.blog_can_not_be_accessed);
                        emailLogService.save(newEmailLog);

                        logger.info("blog can not access notice sent, blog: {}", blog.getDomainName());
                    }
                }
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

    private boolean need2SendEmail(Blog blog, BlogStatus blogStatus) {
        boolean need2SendEmail = false;

        EmailLog emailLog = emailLogService.getLatestByBlogDomainNameAndType(blog.getDomainName(), EmailLog.Type.blog_can_not_be_accessed);

        if (null == emailLog) {
            need2SendEmail = true;
        } else {
            long now = System.currentTimeMillis();
            long sendAt = emailLog.getSendAt().getTime();
            long detectedAt = blogStatus.getDetectedAt().getTime();
            long oneMonth = (long) 30 * 24 * 60 * 60 * 1000;
            long oneYear = 12 * oneMonth;

            need2SendEmail = (now > sendAt)
                    && (now > detectedAt)
                    && ((now - sendAt) > oneMonth)
                    && ((now - detectedAt) < oneYear);
        }

        return need2SendEmail;
    }

}
