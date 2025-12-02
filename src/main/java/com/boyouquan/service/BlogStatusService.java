package com.boyouquan.service;

import com.boyouquan.model.BlogStatus;

import java.util.Date;

public interface BlogStatusService {

    String getUnOkInfo(String blogDomainName, Date collectedAt);

    void detectBlogStatus(Blog blog);

    boolean isStatusOkByBlogDomainName(String blogDomainName);

    boolean isBlogSunset(String blogDomainName);

    BlogStatus getLatestByBlogDomainName(String blogDomainName);

    void save(String blogDomainName, BlogStatus.Status status, int code, String reason);

    void deleteByBlogDomainName(String blogDomainName);

}
