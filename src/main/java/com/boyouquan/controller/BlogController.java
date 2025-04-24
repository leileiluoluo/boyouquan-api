package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.*;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PlanetShuttleService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @GetMapping("")
    public ResponseEntity<?> list(
            @RequestParam(value = "domainName", required = false) String domainName,
            @RequestParam(value = "sort", required = false, defaultValue = "collect_time") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        // single blog
        if (StringUtils.isNotBlank(domainName)) {
            BlogInfo blogInfo = blogService.getBlogInfoByDomainName(domainName);
            if (null == blogInfo) {
                return ResponseUtil.errorResponse(ErrorCode.BLOG_NOT_EXISTS);
            }
            return ResponseEntity.ok(blogInfo);
        }

        // blog list
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        Pagination<BlogInfo> blogs = blogService.listBlogInfosWithKeyWord(BlogSortType.of(sort), keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/random-blogs")
    public ResponseEntity<List<BlogInfo>> getRandomBlogsUnderBlog(@RequestParam("domainName") String domainName) {
        List<BlogInfo> blogInfos = blogService.listByRandom(List.of(domainName), false, 2)
                .stream()
                .map(blog -> blogService.getBlogInfoByDomainName(blog.getDomainName()))
                .toList();

        return ResponseEntity.ok(blogInfos);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPostsUnderBlog(@RequestParam("domainName") String domainName) {
        List<Post> posts = postService.listByDraftAndBlogDomainName(false, domainName, 1, CommonConstants.ALL_POST_COUNT_LIMIT)
                .getResults();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/charts")
    public ResponseEntity<BlogCharts> getBlogCharts(@RequestParam("domainName") String domainName) {
        BlogCharts charts = new BlogCharts();

        // monthly access charts
        List<MonthAccess> monthAccessList = accessService.getBlogAccessSeriesInLatestOneYear(domainName);
        List<String> yearlyAccessDataLabels = monthAccessList.stream().map(MonthAccess::getMonth).toList();
        List<Integer> yearlyAccessDataValues = monthAccessList.stream().map(MonthAccess::getCount).toList();

        charts.setYearlyAccessDataLabels(yearlyAccessDataLabels);
        charts.setYearlyAccessDataValues(yearlyAccessDataValues);

        // planet shuttle charts
        boolean showLatestInitiatedChart = false;
        String latestInitiatedYearMonthStr = planetShuttleService.getLatestInitiatedYearMonthStr(domainName);
        if (StringUtils.isNotBlank(latestInitiatedYearMonthStr)) {
            Date latestInitiated = CommonUtils.yearMonthStr2Date(latestInitiatedYearMonthStr);
            boolean oneYearAgo = CommonUtils.isDateOneYearAgo(latestInitiated);
            if (!oneYearAgo) {
                showLatestInitiatedChart = true;
                List<MonthInitiated> monthInitiatedList = planetShuttleService.getBlogInitiatedSeriesInLatestOneYear(domainName);
                List<String> yearlyInitiatedDataLabels = monthInitiatedList.stream().map(MonthInitiated::getMonth).toList();
                List<Integer> yearlyInitiatedDataValues = monthInitiatedList.stream().map(MonthInitiated::getCount).toList();

                charts.setYearlyInitiatedDataLabels(yearlyInitiatedDataLabels);
                charts.setYearlyInitiatedDataValues(yearlyInitiatedDataValues);
            }
        }
        charts.setShowLatestInitiatedChart(showLatestInitiatedChart);

        // yearly publish charts
        boolean showLatestPublishedAtChart = false;
        Date latestPublishAt = postService.getLatestPublishedAtByBlogDomainName(domainName);
        showLatestPublishedAtChart = !CommonUtils.isDateOneYearAgo(latestPublishAt);
        charts.setShowLatestPublishedAtChart(showLatestPublishedAtChart);
        if (showLatestPublishedAtChart) {
            List<MonthPublish> monthPublishList = postService.getBlogPostPublishSeriesInLatestOneYear(domainName);
            List<String> yearlyPublishDataLabels = monthPublishList.stream().map(MonthPublish::getMonth).toList();
            List<Integer> yearlyPublishDataValues = monthPublishList.stream().map(MonthPublish::getCount).toList();

            charts.setYearlyPublishDataLabels(yearlyPublishDataLabels);
            charts.setYearlyPublishDataValues(yearlyPublishDataValues);
        }

        return ResponseEntity.ok(charts);
    }

}
