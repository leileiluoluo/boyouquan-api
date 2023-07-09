package com.boyouquan.service;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.util.Pagination;

import java.util.Date;

public interface BlogPostService {

    BlogAggregate getBlogByRandom();

    Pagination<BlogAggregate> listBlogsOrderByPostDate(String keyword, int page, int size);

    Pagination<BlogPost> listLatestBlogPostsByAddress(String address, int page, int size);

    Pagination<BlogPost> listBlogPosts(String keyword, int page, int size);

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(String address, Date datePoint);

    Long countBlogs(String keyword);

    int countPosts(String keyword);

}
