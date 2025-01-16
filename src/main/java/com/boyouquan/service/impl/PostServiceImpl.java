package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.PostDaoMapper;
import com.boyouquan.model.*;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.OkHttpUtil;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private PostDaoMapper postDaoMapper;

    @Override
    public List<PostLatestPublishedAt> listPostLatestPublishedAt(int limit) {
        Pagination<Post> postPagination = listWithKeyWord(PostSortType.latest, "", 1, limit);
        if (postPagination.getResults().isEmpty()) {
            return Collections.emptyList();
        }

        return postPagination.getResults()
                .stream()
                .map(post -> {
                    PostLatestPublishedAt postLatestPublishedAt = new PostLatestPublishedAt();
                    String postAbstractPageUrl = String.format("%s?link=%s", CommonConstants.POST_ABSTRACT_ADDRESS, CommonUtils.urlEncode(post.getLink()));
                    postLatestPublishedAt.setPostAbstractPageUrl(postAbstractPageUrl);
                    postLatestPublishedAt.setPublishedAt(CommonUtils.dateSitemapFormatStr(post.getPublishedAt()));

                    return postLatestPublishedAt;
                }).toList();
    }

    @Override
    public void detectPostStatus(String blogDomainName, String link, Date publishedAt) {
        if (CommonUtils.isDateOneWeekAgo(publishedAt)) {
            return;
        }

        try (Response response = requestPostLink(link);
             ResponseBody responseBody = response.body()) {

            int code = response.code();
            String responseBodyString = responseBody.string();
            responseBodyString = responseBodyString.length() > 200 ? responseBodyString.substring(0, 200) : responseBodyString;
            if (HttpStatus.OK.value() != code) {
                logger.info("response code is not 200 (responseBody: {}), post will be deleted!", responseBodyString);
                deleteByLink(link);
            }
        } catch (SocketTimeoutException e) {
            logger.error("timeout exception", e);
        } catch (Exception e) {
            logger.error("exception caught", e);
        }
    }

    @Override
    public BlogDomainNamePublish getMostPublishedInLatestOneMonth() {
        return postDaoMapper.getMostPublishedInLatestOneMonth();
    }

    @Override
    public List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName) {
        return postDaoMapper.getBlogPostPublishSeriesInLatestOneYear(blogDomainName);
    }

    @Override
    public Pagination<Post> listWithKeyWord(PostSortType sort, String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listWithKeyWord(sort.name(), keyword, offset, size);
        Long total = postDaoMapper.countWithKeyWord(sort.name(), keyword);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public Long countAll() {
        return postDaoMapper.countAll();
    }

    @Override
    public Date getLatestPublishedAtByBlogDomainName(String blogDomainName) {
        return postDaoMapper.getLatestPublishedAtByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName) {
        return postDaoMapper.countByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName, Date startDate) {
        return postDaoMapper.countByBlogDomainNameAndStartDate(blogDomainName, startDate);
    }

    @Override
    public Pagination<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listByDraftAndBlogDomainName(draft, blogDomainName, offset, size);
        Long total = postDaoMapper.countByDraftAndBlogDomainName(draft, blogDomainName);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public List<Post> listRecommendedByBlogDomainName(String blogDomainName, Date startDate) {
        return postDaoMapper.listRecommendedByBlogDomainName(blogDomainName, startDate);
    }

    @Override
    public List<BlogPostCount> listBlogPostCount(Date startDate) {
        return postDaoMapper.listBlogPostCount(startDate);
    }

    @Override
    public boolean existsByLink(String link) {
        return postDaoMapper.existsByLink(link);
    }

    @Override
    public boolean existsByTitle(String blogDomainName, String title) {
        return postDaoMapper.existsByTitle(blogDomainName, title);
    }

    @Override
    public Post getByLink(String link) {
        return postDaoMapper.getByLink(link);
    }

    @Override
    public int batchSave(List<Post> posts) {
        int count = 0;

        if (null != posts) {
            for (Post post : posts) {
                try {
                    postDaoMapper.save(post);
                } catch (Exception e) {
                    logger.error("save failed", e);
                    continue;
                }

                count++;
            }
        }

        return count;
    }

    @Override
    public boolean batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft) {
        try {
            postDaoMapper.batchUpdateDraftByBlogDomainName(blogDomainName, draft);
            return true;
        } catch (Exception e) {
            logger.error("batch save failed!", e);
        }
        return false;
    }

    @Override
    public void deleteByBlogDomainName(String blogDomainName) {
        postDaoMapper.deleteByBlogDomainName(blogDomainName);
    }

    @Override
    public void deleteByLink(String link) {
        postDaoMapper.deleteByLink(link);
        logger.info("link deleted! {}", link);
    }

    @Override
    public void recommendByLink(String link) {
        postDaoMapper.recommendByLink(link);
    }

    @Override
    public void unpinByLink(String link) {
        postDaoMapper.unpinByLink(link);
        logger.info("link unpinned! {}", link);
    }

    @Override
    public void pinByLink(String link) {
        postDaoMapper.pinByLink(link);
        logger.info("link pinned! {}", link);
    }

    private Response requestPostLink(String link) throws IOException {
        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(link)
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

}
