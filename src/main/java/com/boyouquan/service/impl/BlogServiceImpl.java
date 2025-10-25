package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogDaoMapper;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDaoMapper blogDaoMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private BlogLocationService blogLocationService;
    @Autowired
    private DomainNameInfoService domainNameInfoService;

    @Override
    public List<BlogLatestPublishedAt> listBlogLatestPublishedAt() {
        return listAll().stream().map(blog -> {
            BlogLatestPublishedAt blogLatestPublishedAt = new BlogLatestPublishedAt();
            Date latestPublishedAt = postService.getLatestPublishedAtByBlogDomainName(blog.getDomainName());

            String blogDetailPageUrl = String.format("%s/%s", CommonConstants.FULL_BLOG_LIST_ADDRESS, blog.getDomainName());
            blogLatestPublishedAt.setBlogDetailPageUrl(blogDetailPageUrl);

            Date date = (null == latestPublishedAt) ? new Date() : latestPublishedAt;
            blogLatestPublishedAt.setLatestPublishedAt(CommonUtils.dateSitemapFormatStr(date));
            return blogLatestPublishedAt;
        }).toList();
    }

    @Override
    public String getBlogAdminMediumImageURLByDomainName(String blogDomainName) {
        Blog blog = getByDomainName(blogDomainName);
        if (null == blog) {
            return "";
        }

        return String.format(CommonConstants.GRAVATAR_ADDRESS_MEDIUM_SIZE, CommonUtils.md5(blog.getAdminEmail()));
    }

    @Override
    public String getBlogAdminLargeImageURLByDomainName(String blogDomainName) {
        Blog blog = getByDomainName(blogDomainName);
        if (null == blog) {
            return "";
        }

        return String.format(CommonConstants.GRAVATAR_ADDRESS_LARGE_SIZE, CommonUtils.md5(blog.getAdminEmail()));
    }

    @Override
    public List<Blog> listByRandom(List<String> excludedDomainNames, boolean gravatarValid, int limit) {
        int tryTimes = 0;
        List<Blog> blogs = Collections.emptyList();
        while (tryTimes < CommonConstants.RANDOM_BLOG_MAX_TRY_TIMES) {
            blogs = blogDaoMapper.listByRandom(excludedDomainNames, gravatarValid, limit);

            boolean existsStatusNotOkBlogs = blogs.stream()
                    .anyMatch(
                            blog -> !blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName())
                    );

            if (!existsStatusNotOkBlogs) {
                return blogs;
            }

            tryTimes++;
        }

        // default
        return blogs;
    }

    @Override
    public Long countAll() {
        return blogDaoMapper.countAll();
    }

    @Override
    public List<Blog> listAll() {
        return blogDaoMapper.listAll();
    }

    @Override
    public List<Blog> listRecentCollected(int limit) {
        return blogDaoMapper.listRecentCollected(limit);
    }

    @Override
    public BlogInfo getBlogInfoByDomainName(String domainName) {
        Blog blog = getByDomainName(domainName);
        if (null == blog) {
            return null;
        }

        // assemble
        return assembleBlogInfo(blog, false, CommonConstants.LATEST_POST_COUNT_LIMIT);
    }

    @Override
    public Pagination<BlogInfo> listBlogInfosWithKeyWord(BlogSortType sort, String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        // list
        List<BlogInfo> blogInfos = new ArrayList<>();
        Pagination<Blog> blogPagination = listWithKeyWord(sort, keyword, page, size);
        for (Blog blog : blogPagination.getResults()) {
            // assemble
            BlogInfo blogInfo = assembleBlogInfo(blog, true, CommonConstants.LATEST_POST_COUNT_LIMIT);
            blogInfos.add(blogInfo);
        }
        long total = blogPagination.getTotal();
        return PaginationBuilder.<BlogInfo>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogInfos).build();
    }

    @Override
    public List<PopularBlogInfo> listPopularBlogInfos(int limit) {
        List<PopularBlog> popularBlogs = blogDaoMapper.listPopularBlogs(limit);

        // list
        List<PopularBlogInfo> popularBlogInfos = new ArrayList<>();
        for (PopularBlog popularBlog : popularBlogs) {
            Blog blog = getByDomainName(popularBlog.getBlogDomainName());

            // assemble
            PopularBlogInfo popularBlogInfo = new PopularBlogInfo();
            BeanUtils.copyProperties(popularBlog, popularBlogInfo);
            popularBlogInfo.setBlogName(blog.getName());
            popularBlogInfo.setBlogAdminLargeImageURL(getBlogAdminLargeImageURLByDomainName(blog.getDomainName()));

            popularBlogInfos.add(popularBlogInfo);
        }
        return popularBlogInfos;
    }

    @Override
    public Pagination<Blog> listWithKeyWord(BlogSortType sort, String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Blog> blogs = blogDaoMapper.listWithKeyWord(sort.name(), keyword, offset, size);
        Long total = blogDaoMapper.countWithKeyword(keyword);
        return PaginationBuilder.<Blog>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogs).build();
    }

    @Override
    public List<BlogCollectedAt> listBlogCollectedAt() {
        return blogDaoMapper.listBlogCollectedAt();
    }

    @Override
    public boolean existsByRssAddress(String rssAddress) {
        return blogDaoMapper.existsByRssAddress(rssAddress);
    }

    @Override
    public boolean existsByDomainName(String domainName) {
        return blogDaoMapper.existsByDomainName(domainName);
    }

    @Override
    public Blog getByDomainName(String domainName) {
        return blogDaoMapper.getByDomainName(domainName);
    }

    @Override
    public List<BlogInfo> listByAdminEmail(String adminEmail) {
        List<Blog> blogs = blogDaoMapper.listByAdminEmail(adminEmail);
        List<BlogInfo> blogInfos = new ArrayList<>();
        for (Blog blog : blogs) {
            // assemble
            BlogInfo blogInfo = assembleBlogInfo(blog, true, CommonConstants.LATEST_POST_COUNT_LIMIT);
            blogInfos.add(blogInfo);
        }
        return blogInfos;
    }

    @Override
    public Blog getByShortDomainName(String shortDomainName) {
        return blogDaoMapper.getByShortDomainName(shortDomainName);
    }

    @Override
    public Blog getByAddress(String address) {
        return blogDaoMapper.getByAddress(address);
    }

    @Override
    public Blog getByRSSAddress(String rssAddress) {
        return blogDaoMapper.getByRSSAddress(rssAddress);
    }

    @Override
    public Blog getByMd5AdminEmail(String md5AdminEmail) {
        return blogDaoMapper.getByMd5AdminEmail(md5AdminEmail);
    }

    @Override
    public Integer getJoinYearsByDomainName(String domainName) {
        return blogDaoMapper.getJoinYearsByDomainName(domainName);
    }

    @Override
    public void save(Blog blog) {
        blogDaoMapper.save(blog);
    }

    @Override
    public void update(Blog blog) {
        assert null != blog
                && StringUtils.isNotBlank(blog.getDomainName());

        blogDaoMapper.update(blog);
    }

    @Override
    public void markAsInvalid(String domainName) {
        blogDaoMapper.markAsInvalid(domainName);
    }

    @Override
    public void updateGravatarValidFlag(String domainName, boolean gravatarValid) {
        blogDaoMapper.updateGravatarValidFlag(domainName, gravatarValid);
    }

    @Override
    public void deleteByDomainName(String domainName) {
        blogDaoMapper.deleteByDomainName(domainName);
    }

    private BlogInfo assembleBlogInfo(Blog blog, boolean withPosts, int postCountLimit) {
        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(blog, blogInfo);

        String blogDomainName = blog.getDomainName();
        Long count = postService.countByBlogDomainName(blogDomainName);
        blogInfo.setPostCount(count);
        Date latestUpdatedAt = postService.getLatestPublishedAtByBlogDomainName(blogDomainName);
        Long accessCount = accessService.countByBlogDomainName(blogDomainName);
        blogInfo.setAccessCount(accessCount);
        blogInfo.setLatestPublishedAt(latestUpdatedAt);

        String blogAdminMediumImageURL = getBlogAdminMediumImageURLByDomainName(blogDomainName);
        blogInfo.setBlogAdminMediumImageURL(blogAdminMediumImageURL);

        String blogAdminLargeImageURL = getBlogAdminLargeImageURLByDomainName(blogDomainName);
        blogInfo.setBlogAdminLargeImageURL(blogAdminLargeImageURL);

        if (withPosts) {
            Pagination<Post> latestPostsPagination = postService.listByDraftAndBlogDomainName(false, blog.getDomainName(), 1, postCountLimit);
            blogInfo.setPosts(latestPostsPagination.getResults());
        }

        blogInfo.setSubmittedInfo(blog.getSelfSubmitted() ? "自行提交" : "后台收录");
        String collectedAt = CommonUtils.dateCommonFormatDisplay(blogInfo.getCollectedAt());
        blogInfo.setSubmittedInfoTip(blog.getSelfSubmitted() ? String.format("该博客由博主自行提交于：%s", collectedAt) : String.format("该博客由本站后台收录于：%s", collectedAt));

        // status
        boolean isStatusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
        blogInfo.setStatusOk(isStatusOk);
        blogInfo.setStatusUnOkInfo(blogStatusService.getUnOkInfo(blogDomainName, blogInfo.getCollectedAt()));
        boolean sunset = blogStatusService.isBlogSunset(blogDomainName);
        blogInfo.setSunset(sunset);

        // blog server location
        BlogLocation blogLocation = blogLocationService.getByDomainName(blogDomainName);
        if (null != blogLocation) {
            blogInfo.setBlogServerLocation(blogLocation.getLocationShort());
        }

        // domain name info
        DomainNameInfo domainNameInfo = domainNameInfoService.getDomainNameInfoByBlogDomainName(blogDomainName);
        if (null != domainNameInfo) {
            blogInfo.setDomainNameRegisteredAt(domainNameInfo.getRegisteredAt());
        }

        return blogInfo;
    }

}
