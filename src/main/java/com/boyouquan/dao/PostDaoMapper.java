package com.boyouquan.dao;

import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.BlogPostCount;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.model.Post;

import java.util.Date;
import java.util.List;

public interface PostDaoMapper {

    BlogDomainNamePublish getMostPublishedInLatestOneMonth();

    List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName);

    Long countWithKeyWord(String sort, String keyword);

    List<Post> listWithKeyWord(String sort, String keyword, int offset, int rows);

    Long countAll();

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    Long countByDraftAndBlogDomainName(boolean draft, String blogDomainName);

    List<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int offset, int rows);

    List<Post> listRecommendedByBlogDomainName(String blogDomainName, Date startDate);

    List<BlogPostCount> listBlogPostCount(Date startDate);

    Post getByLink(String link);

    boolean existsByLink(String link);

    boolean existsByTitle(String blogDomainName, String title);

    void batchSave(List<Post> posts);

    void save(Post post);

    void batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft);

    void deleteByBlogDomainName(String blogDomainName);

    void deleteByLink(String link);

    void recommendByLink(String link);

    void unpinByLink(String link);

    void pinByLink(String link);

}
