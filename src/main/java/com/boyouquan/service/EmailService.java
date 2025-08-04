package com.boyouquan.service;

import com.boyouquan.model.*;

public interface EmailService {

    void sendBlogRequestSubmittedNotice(BlogRequest blogRequest);

    void sendBlogRequestApprovedNotice(BlogRequest blogRequest, Blog blog);

    void sendBlogSystemCollectedNotice(BlogRequest blogRequest, Blog blog);

    void sendBlogRequestRejectNotice(BlogRequest blogRequest);

    void sendPostRecommendedNotice(Blog blog, Post post);

    void sendPostPinnedNotice(Blog blog, Post post);

    void sendBlogStatusNotOkNotice(Blog blog, BlogStatus.Status status, String unOkInfo);

    void sendBlogUncollectedNotice(BlogRequest blogRequest);

    String getBlogAnnualReport(BlogAnnualReport blogAnnualReport);

    void sendBlogAnnualReport(Blog blog, BlogAnnualReport blogAnnualReport);

    void sendEmailValidationCode(String email, String code);

    void sendLatestMonthlySelected(String email, MonthlySelectedPost monthlySelectedPost);

    void send(String to, String subject, String content, boolean html);

}
