package com.boyouquan.dao;

import com.boyouquan.model.FriendLink;

import java.util.List;

public interface FriendLinkDaoMapper {

    List<FriendLink> listAll();

    List<FriendLink> listBySourceBlogDomainName(String domainName);

    void deleteBySourceBlogDomainName(String domainName);

    void save(FriendLink friendLink);

}
