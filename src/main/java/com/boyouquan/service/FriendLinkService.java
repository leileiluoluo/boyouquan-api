package com.boyouquan.service;

import com.boyouquan.model.*;

import java.util.Date;
import java.util.List;

public interface FriendLinkService {

    BlogIntimacy computeBlogIntimacies(String ip, String sourceBlogDomainName, String targetBlogDomainName);

    MyFriendLinks getMyFriendLinks(String blogDomainName);

    List<BlogShortInfo> listAllSourceBlogs();

    List<BlogShortInfo> listAllTargetBlogs();

    Date getJobMaxCreatedAt();

    void detectFriendLinks(Blog blog, boolean triggeredByJob);

    void deleteBySourceBlogDomainName(String domainName);

    void deleteByTargetBlogDomainName(String domainName);

    void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks);

}
