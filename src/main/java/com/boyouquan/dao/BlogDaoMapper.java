package com.boyouquan.dao;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogCollectedAt;

import java.util.List;

public interface BlogDaoMapper {

    List<Blog> listByRandom(List<String> excludedDomainNames, boolean gravatarValid, int limit);

    Long countAll();

    List<Blog> listAll();

    List<Blog> listRecentCollected(int limit);

    Long countWithKeyword(String keyword);

    List<Blog> listWithKeyWord(String sort, String keyword, int offset, int rows);

    List<BlogCollectedAt> listBlogCollectedAt();

    boolean existsByDomainName(String domainName);

    boolean existsByRssAddress(String rssAddress);

    Blog getByDomainName(String domainName);

    List<Blog> listByAdminEmail(String adminEmail);

    Blog getByShortDomainName(String shortDomainName);

    Blog getByAddress(String address);

    Blog getByRSSAddress(String rssAddress);

    Blog getByMd5AdminEmail(String md5AdminEmail);

    void save(Blog blog);

    void update(Blog blog);

    void updateGravatarValidFlag(String domainName, boolean gravatarValid);

    void deleteByDomainName(String domainName);

}
