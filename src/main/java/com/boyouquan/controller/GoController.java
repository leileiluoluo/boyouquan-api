package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.BotUserAgent;
import com.boyouquan.model.Access;
import com.boyouquan.model.Blog;
import com.boyouquan.model.GoLink;
import com.boyouquan.model.Post;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.IPUtil;
import com.boyouquan.util.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/go")
public class GoController {

    private final Logger logger = LoggerFactory.getLogger(GoController.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("")
    public ResponseEntity<?> go(
            @RequestParam("link") String link,
            @RequestParam(value = "from", required = false) String from,
            HttpServletRequest request) {
        GoLink goLink = new GoLink();
        goLink.setLink(CommonConstants.HOME_PAGE_ADDRESS);

        try {
            if (StringUtils.isNotBlank(link)) {
                String ip = IPUtil.getRealIp(request);
                String userAgent = UserAgentUtil.getUserAgent(request);

                logger.info("user agent: {}", userAgent);

                String blogDomainName = getBlogDomainName(ip, link);
                if (StringUtils.isNotBlank(blogDomainName)) {
                    // save access info
                    boolean isBotAgent = BotUserAgent.isBotAgent(userAgent);
                    if (isBotAgent) {
                        logger.info("bot agent, skip saving access info, userAgent: {}", userAgent);
                    } else {
                        saveAccessInfo(ip, link, blogDomainName);
                    }

                    // FIXME: important, use this way to solve path with chinese character issue
                    // FIXME: no need to fix
                    // link = CommonUtils.repairURL(link);
                    goLink.setLink(link);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return ResponseEntity.ok(goLink);
    }

    private String getBlogDomainName(String ip, String link) {
        Post post = postService.getByLink(link);
        String blogDomainName = "";
        if (null != post) {
            blogDomainName = post.getBlogDomainName();
        } else {
            String blogAddress = CommonUtils.removeFromPart(link);
            Blog blog = blogService.getByAddress(blogAddress);
            if (null != blog) {
                blogDomainName = blog.getDomainName();
            }
        }

        return blogDomainName;
    }

    private void saveAccessInfo(String ip, String link, String blogDomainName) {
        // save
        Access access = new Access();
        access.setIp(ip);
        access.setLink(link);
        access.setBlogDomainName(blogDomainName);
        accessService.save(access);
    }
}
