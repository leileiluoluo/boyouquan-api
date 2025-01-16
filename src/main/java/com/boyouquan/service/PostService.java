package com.boyouquan.service;

import com.boyouquan.model.*;
import com.boyouquan.util.Pagination;

import java.util.Date;
import java.util.List;

public interface PostService {

    List<PostLatestPublishedAt> listPostLatestPublishedAt(int limit);

    void detectPostStatus(String blogDomainName, String link, Date publishedAt);

    BlogDomainNamePublish getMostPublishedInLatestOneMonth();

    List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName);

    Pagination<Post> listWithKeyWord(PostSortType sort, String keyword, int page, int size);

    Long countAll();

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName, Date startDate);

    Pagination<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int page, int size);

    List<Post> listRecommendedByBlogDomainName(String blogDomainName, Date startDate);

    List<BlogPostCount> listBlogPostCount(Date startDate);

    boolean existsByLink(String link);

    boolean existsByTitle(String blogDomainName, String title);

    Post getByLink(String link);

    int batchSave(List<Post> posts);

    boolean batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft);

    void deleteByBlogDomainName(String blogDomainName);

    void deleteByLink(String link);

    void recommendByLink(String link);

    void unpinByLink(String link);

    void pinByLink(String link);

}
