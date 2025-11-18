package com.boyouquan.dao;

import com.boyouquan.model.BlogShortInfo;
import com.boyouquan.model.FriendLink;

import java.util.Date;
import java.util.List;

public interface FriendLinkDaoMapper {

    List<FriendLink> listAll();

    List<FriendLink> listBySourceBlogDomainName(String domainName);

    FriendLink getBySourceAndTargetBlogDomainName(String sourceDomainName, String targetDomainName);

    List<String> getDirectTargetBlogDomainNames(String domainName);

    List<String> getDirectSourceBlogDomainNames(String domainName);

    List<BlogShortInfo> listAllSourceBlogs();

    List<BlogShortInfo> listAllTargetBlogs();

    Date getJobMaxCreatedAt();

    void deleteBySourceBlogDomainName(String domainName);

    void deleteByTargetBlogDomainName(String domainName);

    void save(FriendLink friendLink);

}
