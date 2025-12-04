package com.boyouquan.service.impl;

import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnnualReportServiceImpl implements AnnualReportService {

    private final Logger logger = LoggerFactory.getLogger(AnnualReportServiceImpl.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PinHistoryService pinHistoryService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @Autowired
    private EmailService emailService;

    private final int[] THRESHOLDS = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95};
    private final String[] PERCENTAGES = {"95%", "90%", "85%", "80%", "75%", "70%", "65%", "60%", "65%", "50%", "45%", "40%", "35%", "30%", "25%", "20%", "15%", "10%", "5%"};

    private final List<String> SPONSORS_2024 = List.of(
            "blog.cuger.cn",
            "www.evan.xin",
            "blog.goodboyboy.top",
            "pinaland.cn",
            "www.xiangshitan.com",
            "pwsz.com",
            "vrast.cn",
            "www.dao.js.cn",
            "www.dolingou.com",
            "inkcodes.com",
            "www.feinews.com"
    );

    @Override
    public String getAnnualReport(String domainName) {
        Blog blog = blogService.getByDomainName(domainName);
        if (null == blog) {
            return null;
        }

        // get annual report
        BlogAnnualReport annualReport = getAnnualReport(domainName, blog);

        return emailService.getBlogAnnualReport(annualReport);
    }

    @Override
    public void sendAnnualReport(String domainName) {
        Blog blog = blogService.getByDomainName(domainName);
        if (null == blog) {
            logger.error("blog not found, domainName: {}", domainName);
            return;
        }

        // get annual report
        BlogAnnualReport annualReport = getAnnualReport(domainName, blog);

        // send
        emailService.sendBlogAnnualReport(blog, annualReport);
    }

    private BlogAnnualReport getAnnualReport(String domainName, Blog blog) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        BlogAnnualReport report = new BlogAnnualReport();
        report.setYear(currentYear);
        report.setBlogDomainName(domainName);
        report.setBlogName(blog.getName());
        report.setBlogCollectedAt(CommonUtils.dateCommonFormatDisplay(blog.getCollectedAt()));

        // joinDaysTillNow
        long joinDaysTillNow = ChronoUnit.DAYS.between(blog.getCollectedAt().toInstant(), Instant.now());
        report.setJoinDaysTillNow(joinDaysTillNow);

        // collectedAtExceedPercent
        String collectedAtExceedPercent = getCollectedAtExceedPercent(blog.getCollectedAt());
        report.setCollectedAtExceedPercent(collectedAtExceedPercent);

        Date currentYearFirstDay = getCurrentYearFirstDay();

        // joinedAfterStartDay
        boolean joinedAfterStartDay = blog.getCollectedAt().after(currentYearFirstDay);
        report.setJoinedAfterYearStartDay(joinedAfterStartDay);

        // startDate
        Date startDate = joinedAfterStartDay ? blog.getCollectedAt() : currentYearFirstDay;

        // postCountTillNow
        long postCountTillNow = postService.countByBlogDomainName(domainName, startDate);
        report.setPostCountTillNow(postCountTillNow);

        // postCountExceedPercent
        String postCountExceedPercent = getPostCountExceedPercent(currentYearFirstDay, postCountTillNow);
        report.setPostCountExceedPercent(postCountExceedPercent);

        // accessCountTillNow
        long accessCountTillNow = accessService.countByBlogDomainName(domainName, startDate);
        report.setAccessCountTillNow(accessCountTillNow);

        // accessCountExceedPercent
        String accessCountExceedPercent = getAccessCountExceedPercent(currentYearFirstDay, accessCountTillNow);
        report.setAccessCountExceedPercent(accessCountExceedPercent);

        // maxMonthPublish
        MonthPublish maxMonthPublish = getMaxMonthPublish(domainName, startDate);
        report.setMaxMonthPublish(maxMonthPublish);

        // maxMonthAccess
        MonthAccess maxMonthAccess = getMaxMonthAccess(domainName, startDate);
        report.setMaxMonthAccess(maxMonthAccess);

        // mostAccessedPost
        BlogLinkAccess blogLinkAccess = accessService.getMostAccessedLinkByDomainName(domainName, blog.getAddress(), startDate);
        if (null != blogLinkAccess) {
            Post post = postService.getByLink(blogLinkAccess.getLink());
            if (null != post) {
                report.setMostAccessedPost(post);
                report.setMostAccessedPostAccessCount(blogLinkAccess.getAccessCount());
            }
        }

        // latestUpdatedAt
        Date latestUpdatedAt = postService.getLatestPublishedAtByBlogDomainName(domainName);
        if (null != latestUpdatedAt) {
            report.setLatestUpdatedAt(CommonUtils.dateFriendlyDisplay(latestUpdatedAt));
        }

        // mostLatePublishedPost
        Post mostLatePublishedPost = getMostLatePublishedPost(domainName, startDate);
        if (null != mostLatePublishedPost) {
            report.setMostLatePublishedPost(mostLatePublishedPost);
            report.setMostLatePublishedPostPublishedAt(CommonUtils.dateHourSecondCommonFormatDisplay(mostLatePublishedPost.getPublishedAt()));
        }

        // recommendedPosts
        List<Post> recommendedPosts = postService.listRecommendedByBlogDomainName(domainName, startDate);
        report.setRecommendedPosts(recommendedPosts);

        // pinnedPosts
        List<Post> pinnedPosts = getPinnedPosts(domainName, startDate);
        report.setPinnedPosts(pinnedPosts);

        // planetShuttleInitiatedCount
        long planetShuttleInitiatedCount = getPlanetShuttleInitiatedCount(domainName, startDate);
        report.setPlanetShuttleInitiatedCount(planetShuttleInitiatedCount);

        // isSponsor
        report.setSponsor(isSponsor(domainName, SPONSORS_2024));

        return report;
    }

    private Date getCurrentYearFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    private MonthPublish getMaxMonthPublish(String domainName, Date startDate) {
        List<MonthPublish> monthPublishes = postService.getBlogPostPublishSeriesInLatestOneYear(domainName);
        if (null != monthPublishes && !monthPublishes.isEmpty()) {
            MonthPublish monthPublish = monthPublishes.stream().max(Comparator.comparingInt(MonthPublish::getCount)).get();
            if (monthPublish.getCount() > 0) {
                try {
                    Date monthDate = new SimpleDateFormat("yyyy/MM").parse(monthPublish.getMonth());
                    return monthDate.after(startDate) ? monthPublish : null;
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private Post getMostLatePublishedPost(String domainName, Date startDate) {
        Pagination<Post> pagination = postService.listByDraftAndBlogDomainName(false, domainName, 1, 1000);
        List<Post> posts = pagination.getResults();
        if (null != posts && !posts.isEmpty()) {
            Optional<Post> minNightPublished = posts.stream()
                    .filter(post -> post.getPublishedAt().after(startDate))
                    .filter(post -> post.getPublishedAt().getHours() >= 21)
                    .max(Comparator.comparingInt(post -> post.getPublishedAt().getHours()));

            Optional<Post> earlyMorningPublished = posts.stream()
                    .filter(post -> post.getPublishedAt().after(startDate))
                    .filter(post -> post.getPublishedAt().getHours() < 5)
                    .min(Comparator.comparingInt(post -> post.getPublishedAt().getHours()));

            if (earlyMorningPublished.isPresent()) {
                return earlyMorningPublished.get();
            }
            if (minNightPublished.isPresent()) {
                return minNightPublished.get();
            }
        }
        return null;
    }

    private MonthAccess getMaxMonthAccess(String domainName, Date startDate) {
        List<MonthAccess> monthAccesses = accessService.getBlogAccessSeriesInLatestOneYear(domainName);
        if (null != monthAccesses && !monthAccesses.isEmpty()) {
            MonthAccess monthAccess = monthAccesses.stream().max(Comparator.comparingInt(MonthAccess::getCount)).get();
            if (monthAccess.getCount() > 0) {
                try {
                    Date monthDate = new SimpleDateFormat("yyyy/MM").parse(monthAccess.getMonth());
                    return monthDate.after(startDate) ? monthAccess : null;
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private List<Post> getPinnedPosts(String domainName, Date startDate) {
        List<String> pinnedLinks = pinHistoryService.listLinksByBlogDomainName(domainName, startDate);
        List<Post> pinnedPosts = new ArrayList<>();
        if (null != pinnedLinks) {
            for (String link : pinnedLinks) {
                Post post = postService.getByLink(link);
                if (null != post) {
                    pinnedPosts.add(post);
                }
            }
        }
        return pinnedPosts;
    }

    private long getPlanetShuttleInitiatedCount(String domainName, Date startDate) {
        List<MonthInitiated> monthInitiated = planetShuttleService.getBlogInitiatedSeriesInLatestOneYear(domainName);
        if (null != monthInitiated && !monthInitiated.isEmpty()) {
            return monthInitiated.stream().mapToInt(MonthInitiated::getCount).sum();
        }
        return 0L;
    }

    private boolean isSponsor(String domainName, List<String> sponsors) {
        return sponsors.contains(domainName);
    }

    private String getPostCountExceedPercent(Date startDate, long count) {
        List<Long> blogPostCounts = postService.listBlogPostCount(startDate)
                .stream()
                .map(BlogPostCount::getCount)
                .toList();

        if (blogPostCounts.isEmpty()
                || blogPostCounts.size() == 1) {
            return "0%";
        }

        int size = blogPostCounts.size() - 1;
        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (count > blogPostCounts.get(getIndex(size, THRESHOLDS[i]))) {
                return PERCENTAGES[i];
            }
        }
        return "0%";
    }

    private String getAccessCountExceedPercent(Date startDate, long count) {
        List<Long> blogAccessCounts = accessService.listBlogAccessCount(startDate)
                .stream()
                .map(BlogAccessCount::getCount)
                .toList();

        if (blogAccessCounts.isEmpty()
                || blogAccessCounts.size() == 1) {
            return "0%";
        }

        int size = blogAccessCounts.size() - 1;
        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (count > blogAccessCounts.get(getIndex(size, THRESHOLDS[i]))) {
                return PERCENTAGES[i];
            }
        }
        return "0%";
    }

    private String getCollectedAtExceedPercent(Date collectedAt) {
        List<Date> blogCollectedAts = blogService.listBlogCollectedAt()
                .stream()
                .map(BlogCollectedAt::getCollectedAt)
                .toList();

        if (blogCollectedAts.isEmpty()
                || blogCollectedAts.size() == 1) {
            return "0%";
        }

        int size = blogCollectedAts.size() - 1;
        for (int i = 0; i < THRESHOLDS.length; i++) {
            Date collectedAtSegment = blogCollectedAts.get(getIndex(size, THRESHOLDS[i]));
            if (collectedAt.before(collectedAtSegment)) {
                return PERCENTAGES[i];
            }
        }
        return "0%";
    }

    private int getIndex(int size, int percent) {
        return size * percent / 100;
    }

}
