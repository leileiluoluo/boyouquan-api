package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.model.Post;
import com.boyouquan.model.PostSortType;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.FeedService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class FeedServiceImpl implements FeedService {

    private static final Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

    private static final Pattern ILLEGAL_XML_CHARS =
            Pattern.compile("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]");

    @Autowired
    private PostService postService;
    @Autowired
    private BlogService blogService;

    @Override
    public String generateFeedXML(PostSortType sortType) {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("博友圈");
        feed.setDescription("博客人的朋友圈");
        feed.setLink(CommonConstants.HOME_PAGE_ADDRESS);
        if (PostSortType.recommended.equals(sortType)) {
            feed.setTitle("博友圈 - 推荐文章聚合");
            feed.setDescription("聚合展示博友圈推荐文章");
            feed.setLink(CommonConstants.HOME_PAGE_SORT_RECOMMENDED_ADDRESS);
        } else if (PostSortType.latest.equals(sortType)) {
            feed.setTitle("博友圈 - 最新文章聚合");
            feed.setDescription("聚合展示博友圈最新文章");
            feed.setLink(CommonConstants.HOME_PAGE_SORT_LATEST_ADDRESS);
        }

        try {
            Pagination<Post> posts = postService.listWithKeyWord(sortType, "", CommonConstants.FEED_POST_QUERY_PAGE_NO, CommonConstants.FEED_POST_QUERY_PAGE_SIZE);

            List<SyndEntry> entries = posts.getResults().stream()
                    .map(this::getEntry).toList();

            feed.setEntries(entries);

            return new SyndFeedOutput().outputString(feed);
        } catch (FeedException e) {
            logger.error(e.getMessage(), e);
        }

        return "";
    }

    private SyndEntry getEntry(Post post) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(post.getTitle());
        String postLink = CommonUtils.urlEncode(post.getLink());
        String link = String.format("%s?from=feed&link=%s", CommonConstants.GO_PAGE_ADDRESS, postLink);
        entry.setLink(link);

        SyndContent description = new SyndContentImpl();
        description.setType("text/plain");
        String cleanDescription = sanitizeXmlContent(post.getDescription());
        description.setValue(cleanDescription);
        entry.setDescription(description);
        entry.setPublishedDate(post.getPublishedAt());

        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        if (null != blog) {
            entry.setAuthor(blog.getName());
            entry.setTitle(String.format("%s：%s", blog.getName(), post.getTitle()));
        }

        return entry;
    }

    private String sanitizeXmlContent(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        String cleaned = ILLEGAL_XML_CHARS.matcher(content).replaceAll("");
        return StringEscapeUtils.escapeXml11(cleaned);
    }

}
