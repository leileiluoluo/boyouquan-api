package com.boyouquan.scheduler;

import com.boyouquan.model.Post;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostStatusScheduler {

    private final Logger logger = LoggerFactory.getLogger(PostStatusScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private PostService postService;

    @Scheduled(cron = "0 0 0/8 * * ?")
    public void processPostStatuses() {
        logger.info("post status scheduler start!");

        detectPostStatuses();

        logger.info("post status scheduler end!");
    }

    private void detectPostStatuses() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("process for {}", blog.getDomainName());

                boolean statusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
                if (statusOk) {
                    Pagination<Post> posts = postService.listByDraftAndBlogDomainName(false, blog.getDomainName(), 1, 3);

                    posts.getResults().forEach(
                            post -> postService.detectPostStatus(blog.getDomainName(), post.getLink(), post.getPublishedAt())
                    );
                }
            } catch (Exception e) {
                logger.error("process failed", e);
            }
        }
    }

}

