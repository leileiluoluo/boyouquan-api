package com.boyouquan.constant;

public class CommonConstants {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String DATA_SPIDER_USER_AGENT = "Mozilla/5.0 (compatible; Boyouquanspider/1.0; +https://www.boyouquan.com/about#data-spider)";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String SCHEME_HTTP = "http";

    public static final String FAKE_BLOG_ADMIN_EMAIL = "notfound@notfound.com";
    public static final String FAKE_BLOG_ADMIN_EMAIL_PREFIX = "notfound";

    public static final String BLOG_ADDED_WELCOME_PATTERN = "恭喜「%s」被本站收录！";

    public static final int BLOG_ADDED_LIMIT_SIZE = 1;
    public static final String MOST_ACCESSED_BLOG_ANNOUNCE_PATTERN = "上个月浏览最多的博客是「%s」，浏览 %d 次！";
    public static final String MOST_UPDATED_BLOG_ANNOUNCE_PATTERN = "最近一个月更博最频繁的博友是「%s」，更博 %d 篇！";
    public static final String MOST_INITIATED_BLOG_ANNOUNCE_PATTERN = "上个月星球穿梭助力最多的博友是「%s」，助力 %d 次！";
    public static final String BLOG_LIST_ADDRESS_SORT_BY_COLLECT_TIME = "/blogs";
    public static final String BLOG_ITEM_ADDRESS_PATTERN = "/blogs?sort=access_count&keyword=%s";

    public static final String GRAVATAR_STORE_FOLDER = "gravatar/%s/";
    public static final String GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL = "ccd670b883d4ac11d698c380668529de";
    public static final int GRAVATAR_IMAGE_MEDIUM_SIZE = 40;
    public static final int GRAVATAR_IMAGE_LARGE_SIZE = 80;
    public static final String GRAVATAR_ADDRESS_MEDIUM_SIZE = "/gravatar/%s?size=" + GRAVATAR_IMAGE_MEDIUM_SIZE;
    public static final String GRAVATAR_ADDRESS_LARGE_SIZE = "/gravatar/%s?size=" + GRAVATAR_IMAGE_LARGE_SIZE;

    public static final int RSS_POST_COUNT_READ_LIMIT_FIRST_TIME = 1000;
    public static final int RSS_POST_COUNT_READ_LIMIT = 10;
    public static final int RSS_BLOG_DESCRIPTION_LENGTH_LIMIT = 800;
    public static final int LATEST_POST_COUNT_LIMIT = 3;
    public static final int ALL_POST_COUNT_LIMIT = 10000;

    public static final String HOME_PAGE_ADDRESS = "https://www.boyouquan.com/home";
    public static final String HOME_PAGE_SORT_LATEST_ADDRESS = "https://www.boyouquan.com/home?sort=latest";
    public static final String HOME_PAGE_SORT_RECOMMENDED_ADDRESS = "https://www.boyouquan.com/home?sort=recommended";
    public static final String GO_PAGE_ADDRESS = "https://www.boyouquan.com/go";
    public static final String FULL_BLOG_LIST_ADDRESS = "https://www.boyouquan.com/blogs";
    public static final String POST_ABSTRACT_ADDRESS = "https://www.boyouquan.com/abstract";

    public static final int FEED_POST_QUERY_PAGE_NO = 1;
    public static final int FEED_POST_QUERY_PAGE_SIZE = 100;

    public static final int RANDOM_BLOG_MAX_TRY_TIMES = 3;

    public static final int POPULAR_BLOGGERS_SIZE = 15;

    public static final int MONTHLY_SELECTED_POSTS_LIMIT = 10;
    public static final int MONTHLY_SELECTED_PAGE_SIZE = 1;

    public static final int EMAIL_VALIDATION_CODE_ONE_DAY_LIMIT = 5;

    public static final int SAME_IP_SUBSCRIPTION_COUNT_LIMIT = 2;

    public static final int POST_IMAGES_SCRAPER_SIZE = 20;

    public static final int POST_IMAGES_SIZE_LIMIT = 200;

    public static final int POST_IMAGES_WIDTH_LIMIT = 500;

    public static final String POST_IMAGES_URL_PREFIX = "/images/";

}

