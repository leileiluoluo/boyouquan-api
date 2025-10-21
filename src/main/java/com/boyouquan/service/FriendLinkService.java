package com.boyouquan.service;

import com.boyouquan.model.*;

import java.util.List;
import java.util.Set;

public interface FriendLinkService {

    BlogIntimacy computeBlogIntimacies(String ip, String sourceBlogDomainName, String targetBlogDomainName);

    MyFriendLinks getMyFriendLinks(String blogDomainName);

    List<BlogShortInfo> listAllSourceBlogs();

    List<BlogShortInfo> listAllTargetBlogs();

    void detectFriendLinks(Set<String> blogDomainNames, Blog blog);

    void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks);

}
