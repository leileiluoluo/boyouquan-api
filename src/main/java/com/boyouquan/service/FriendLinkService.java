package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.FriendLink;

import java.util.List;
import java.util.Set;

public interface FriendLinkService {

    void detectFriendLinks(Set<String> blogAddresses, Blog blog);

    void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks);

}
