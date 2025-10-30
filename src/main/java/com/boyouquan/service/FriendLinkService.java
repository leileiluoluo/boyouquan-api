package com.boyouquan.service;

import com.boyouquan.model.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface FriendLinkService {

    BlogIntimacy computeBlogIntimacies(String ip, String sourceBlogDomainName, String targetBlogDomainName);

    MyFriendLinks getMyFriendLinks(String blogDomainName);

    List<BlogShortInfo> listAllSourceBlogs();

    List<BlogShortInfo> listAllTargetBlogs();

    Date getMaxCreatedAt();

    void detectFriendLinks(Set<String> blogDomainNames, Blog blog);

    void deleteBySourceBlogDomainName(String domainName);

    void deleteByTargetBlogDomainName(String domainName);

    void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks);

}
