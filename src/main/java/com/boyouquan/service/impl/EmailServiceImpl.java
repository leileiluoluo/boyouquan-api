package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.EncryptionHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private EncryptionHelper encryptionHelper;

    @Override
    public void sendBlogRequestSubmittedNotice(BlogRequest blogRequest) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String siteManagerEmail = boYouQuanConfig.getEmailUsername();
            String subject = String.format("[博友圈] 新提交的博客「%s」需要审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_add", context);

            // send
            send(siteManagerEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogRequestApprovedNotice(BlogRequest blogRequest, Blog blog) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 恭喜您！您的博客「%s」已通过审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_approved", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogSystemCollectedNotice(BlogRequest blogRequest, Blog blog) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            if (!CommonConstants.FAKE_BLOG_ADMIN_EMAIL.equals(adminEmail)) {
                String subject = String.format("[博友圈] 恭喜您！您的博客「%s」已被博友圈收录！", blogRequest.getName());

                Context context = new Context();
                BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
                BeanUtils.copyProperties(blogRequest, blogRequestInfo);
                if (null != blog) {
                    blogRequestInfo.setDomainName(blog.getDomainName());
                }

                context.setVariable("blogRequestInfo", blogRequestInfo);

                String text = templateEngine.process("email/blog_request_template_collected", context);

                // send
                send(adminEmail, subject, text, true);
            }
        }
    }

    @Override
    public void sendBlogRequestRejectNotice(BlogRequest blogRequest) {
        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 很抱歉！您的博客「%s」未通过审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_rejected", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendPostRecommendedNotice(Blog blog, Post post) {
        if (null != post && null != blog) {
            String adminEmail = blog.getAdminEmail();
            String subject = "[博友圈] 您好！您有一篇文章被博友圈加入到了首页推荐！";

            Context context = new Context();
            context.setVariable("blog", blog);
            context.setVariable("post", post);

            String text = templateEngine.process("email/post_recommended_template", context);

            // send
            // FIXME: temp turn off
            // send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendPostPinnedNotice(Blog blog, Post post) {
        if (null != post && null != blog) {
            String adminEmail = blog.getAdminEmail();
            String subject = "[博友圈] 恭喜您！您有一篇文章被博友圈置顶到了首页推荐！";

            Context context = new Context();
            context.setVariable("blog", blog);
            context.setVariable("post", post);

            String text = templateEngine.process("email/post_pinned_template", context);

            // send
            // FIXME: temp turn off
            // send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogStatusNotOkNotice(Blog blog, BlogStatus.Status status, String unOkInfo) {
        if (null != blog && !BlogStatus.Status.ok.equals(status)) {
            String adminEmail = blog.getAdminEmail();

            String reason = "无法访问";
            if (BlogStatus.Status.timeout.equals(status)) {
                reason = "访问超时";
            }

            String subject = "[博友圈] 告警！博友圈检测到您的博客" + reason + "！";

            Context context = new Context();
            context.setVariable("blog", blog);
            context.setVariable("reason", reason);
            context.setVariable("unOkInfo", unOkInfo);

            String text = templateEngine.process("email/blog_can_not_be_accessed_template", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogUncollectedNotice(BlogRequest blogRequest) {
        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 很遗憾！您的博客「%s」被博友圈移出收录名单！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_uncollected_template", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public String getBlogAnnualReport(BlogAnnualReport blogAnnualReport) {
        Context context = new Context();
        context.setVariable("report", blogAnnualReport);

        return templateEngine.process("email/blog_annual_report_template", context);
    }

    @Override
    public void sendBlogAnnualReport(Blog blog, BlogAnnualReport blogAnnualReport) {
        if (null != blog && null != blogAnnualReport) {
            String adminEmail = blog.getAdminEmail();
            String subject = "[博友圈] 您的 2025 年度报告已揭晓！";

            Context context = new Context();

            context.setVariable("report", blogAnnualReport);

            String text = templateEngine.process("email/blog_annual_report_template", context);

            // send
            send("leileiluoluo@leileiluoluo.com", subject, text, true);
        }
    }

    @Override
    public void sendEmailValidationCode(String email, String code) {
        String subject = "[博友圈] 博主邮箱验证码";

        Context context = new Context();

        context.setVariable("email", email);
        context.setVariable("code", code);

        String text = templateEngine.process("email/email_validation_code_template", context);

        // send
        send(email, subject, text, true);
    }

    @Override
    public void sendLatestMonthlySelected(String email, MonthlySelectedPost monthlySelectedPost) {
        String subject = String.format("[博友圈] %s 精选文章", monthlySelectedPost.getYearMonthStr());

        Context context = new Context();

        context.setVariable("email", email);
        context.setVariable("token", encryptionHelper.encrypt(email));
        context.setVariable("monthlySelectedPost", monthlySelectedPost);

        String text = templateEngine.process("email/monthly_selected_subscription_template", context);

        // send
        send(email, subject, text, true);
    }

    @Override
    public void send(String to, String subject, String content, boolean html) {
        try {
            if (to.equals(CommonConstants.FAKE_BLOG_ADMIN_EMAIL)
                    || to.startsWith(CommonConstants.FAKE_BLOG_ADMIN_EMAIL_PREFIX)) {
                return;
            }

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);
            message.setFrom(boYouQuanConfig.getEmailUsername());

            // send
            javaMailSender.send(message);

            logger.info("email successfully sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("email sent failed!", e);
        }
    }

}
