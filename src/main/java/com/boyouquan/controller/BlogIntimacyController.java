package com.boyouquan.controller;

import com.boyouquan.model.BlogIntimacy;
import com.boyouquan.model.BlogIntimacySearchHistoryInfo;
import com.boyouquan.model.BlogShortInfo;
import com.boyouquan.model.MyFriendLinks;
import com.boyouquan.service.BlogIntimacySearchHistoryService;
import com.boyouquan.service.FriendLinkService;
import com.boyouquan.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blog-intimacies")
public class BlogIntimacyController {

    @Autowired
    private FriendLinkService friendLinkService;
    @Autowired
    private BlogIntimacySearchHistoryService blogIntimacySearchHistoryService;

    @GetMapping("/all-source-blogs")
    public List<BlogShortInfo> listAllSourceBlogs() {
        return friendLinkService.listAllSourceBlogs();
    }

    @GetMapping("/all-target-blogs")
    public List<BlogShortInfo> listAllTargetBlogs() {
        return friendLinkService.listAllTargetBlogs();
    }

    @GetMapping("")
    public ResponseEntity<?> getBlogIntimacies(
            @RequestParam("sourceDomainName") String sourceDomainName,
            @RequestParam("targetDomainName") String targetDomainName,
            HttpServletRequest request) {
        String ip = IPUtil.getRealIp(request);

        BlogIntimacy blogIntimacy = friendLinkService.computeBlogIntimacies(ip, sourceDomainName, targetDomainName);

        return ResponseEntity.ok(blogIntimacy);
    }

    @GetMapping("/my-friend-links")
    public ResponseEntity<?> getMyFriendLinks(
            @RequestParam("blogDomainName") String blogDomainName) {
        MyFriendLinks myFriendLinks = friendLinkService.getMyFriendLinks(blogDomainName);

        return ResponseEntity.ok(myFriendLinks);
    }

    @GetMapping("/search-histories")
    public ResponseEntity<?> getBlogIntimacySearchHistories(
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<BlogIntimacySearchHistoryInfo> histories = blogIntimacySearchHistoryService.listLatestMessages(size);

        return ResponseEntity.ok(histories);
    }

}
