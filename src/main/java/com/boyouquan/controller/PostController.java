package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.*;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import com.boyouquan.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private BlogStatusService blogStatusService;

    @GetMapping("")
    public ResponseEntity<Pagination<PostInfo>> list(
            @RequestParam(value = "sort", required = false, defaultValue = "recommended") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        // posts
        Pagination<Post> postPagination = postService.listWithKeyWord(PostSortType.of(sort), keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        List<PostInfo> postInfos = new ArrayList<>();
        for (Post post : postPagination.getResults()) {
            PostInfo postInfo = new PostInfo();
            BeanUtils.copyProperties(post, postInfo);

            Blog blog = blogService.getByDomainName(post.getBlogDomainName());
            postInfo.setBlogName(blog.getName());
            postInfo.setBlogAddress(blog.getAddress());

            boolean isStatusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
            postInfo.setBlogStatusOk(isStatusOk);

            String blogAdminLargeImageURL = blogService.getBlogAdminLargeImageURLByDomainName(blog.getDomainName());
            postInfo.setBlogAdminLargeImageURL(blogAdminLargeImageURL);

            String blogAdminMediumImageURL = blogService.getBlogAdminMediumImageURLByDomainName(blog.getDomainName());
            postInfo.setBlogAdminMediumImageURL(blogAdminMediumImageURL);

            Long linkAccessCount = accessService.countByLink(post.getLink());
            postInfo.setLinkAccessCount(linkAccessCount);

            Integer joinYears = blogService.getJoinYearsByDomainName(blog.getDomainName());
            postInfo.setBlogJoinYears(joinYears);

            Long totalAccessCount = accessService.countByBlogDomainName(blog.getDomainName());
            postInfo.setBlogTotalAccessCount(totalAccessCount);
            postInfos.add(postInfo);
        }

        Pagination<PostInfo> postInfoPagination = PaginationBuilder.<PostInfo>newBuilder()
                .pageNo(page)
                .pageSize(postPagination.getPageSize())
                .total(postPagination.getTotal())
                .results(postInfos).build();

        return ResponseEntity.ok(postInfoPagination);
    }

    @GetMapping("/by-link")
    public ResponseEntity<?> getByLink(@RequestParam("link") String link) {
        Post post = postService.getByLink(link);

        if (null == post) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        PostInfo postInfo = new PostInfo();
        BeanUtils.copyProperties(post, postInfo);

        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        postInfo.setBlogName(blog.getName());
        postInfo.setBlogAddress(blog.getAddress());

        boolean isStatusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
        postInfo.setBlogStatusOk(isStatusOk);

        String blogAdminLargeImageURL = blogService.getBlogAdminLargeImageURLByDomainName(blog.getDomainName());
        postInfo.setBlogAdminLargeImageURL(blogAdminLargeImageURL);

        Long linkAccessCount = accessService.countByLink(post.getLink());
        postInfo.setLinkAccessCount(linkAccessCount);

        Integer joinYears = blogService.getJoinYearsByDomainName(blog.getDomainName());
        postInfo.setBlogJoinYears(joinYears);

        Long totalAccessCount = accessService.countByBlogDomainName(blog.getDomainName());
        postInfo.setBlogTotalAccessCount(totalAccessCount);

        return ResponseEntity.ok(postInfo);
    }

}
