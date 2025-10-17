package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.FriendLinkDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.FriendLink;
import com.boyouquan.service.FriendLinkService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkDaoMapper friendLinkDaoMapper;

    @Override
    public void detectFriendLinks(Set<String> blogAddresses, Blog blog) {
        Set<String> internalLinks = Collections.emptySet();

        try {
            Document doc = Jsoup.connect(blog.getAddress())
                    .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                    .timeout(10000).get();

            internalLinks = doc.select("a[href]").stream()
                    .map(a -> a.absUrl("href"))
                    .filter(url -> url.startsWith(blog.getAddress()))
                    .limit(100)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("connect failed", e);
        }

        List<FriendLink> friendLinks = new ArrayList<>();
        for (String internalLink : internalLinks) {
            try {
                Document page = Jsoup.connect(internalLink)
                        .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                        .timeout(10000).get();
                String title = Optional.of(page.title()).orElse("无标题");

                for (String targetUrl : blogAddresses) {
                    if (!targetUrl.equals(blog.getAddress())
                            && !targetUrl.contains(blog.getDomainName())
                            && page.outerHtml().contains(targetUrl)) {
                        FriendLink link = new FriendLink();
                        link.setSourceBlogDomainName(blog.getDomainName());
                        link.setTargetBlogDomainName(targetUrl);
                        link.setPageTitle(title);
                        link.setPageUrl(internalLink);

                        friendLinks.add(link);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        // save
        saveOrUpdate(blog.getDomainName(), friendLinks);
    }

    @Override
    public void saveOrUpdate(String sourceBlogDomainName, List<FriendLink> friendLinks) {
        if (friendLinks.isEmpty()) {
            return;
        }

        log.info("sourceBlogDomainName: {}, friendLinks size: {}", sourceBlogDomainName, friendLinks.size());

        friendLinkDaoMapper.deleteBySourceBlogDomainName(sourceBlogDomainName);

        for (FriendLink link : friendLinks) {
            friendLinkDaoMapper.save(link);
        }
    }

}
