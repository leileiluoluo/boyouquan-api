package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.PostHelper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.model.WebSocketMessage;
import com.boyouquan.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostScheduler {

    private final Logger logger = LoggerFactory.getLogger(PostScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogCrawlerService blogCrawlerService;
    @Autowired
    private PostHelper postHelper;
    @Autowired
    private WebSocketService webSocketService;

    @Scheduled(cron = "0 0 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    public void crawlingBlogPosts() {
        logger.info("post scheduler start!");

        savePosts();

        logger.info("post scheduler end!");
    }

    public void savePosts() {
        List<Blog> blogs = blogService.listAll();
        int total = 0;
        for (Blog blog : blogs) {
            try {
                boolean statusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
                if (statusOk) {
                    logger.info("start crawling posts, blogDomainName: {}", blog.getDomainName());

                    RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(blog.getRssAddress(), CommonConstants.RSS_POST_COUNT_READ_LIMIT);

                    // save posts
                    int count = postHelper.savePosts(blog.getDomainName(), rssInfo, false);
                    if (0 == count) {
                        logger.info("no new posts saved, blogDomainName: {}", blog.getDomainName());
                        continue;
                    }

                    total += count;
                    logger.info("{} posts saved, blogDomainName: {}", count, blog.getDomainName());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        // broadcast
        if (total > 0) {
            broadcast(total);
        }
    }

    private void broadcast(int total) {
        WebSocketMessage message = new WebSocketMessage();
        message.setMessage(String.format("刚刚新收录了 %d 篇文章，快来看看吧！", total));
        message.setGotoUrl(CommonConstants.HOME_PAGE_SORT_LATEST_ADDRESS);
        webSocketService.broadcast(message);
    }

}

