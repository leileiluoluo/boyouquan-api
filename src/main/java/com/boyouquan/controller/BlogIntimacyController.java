package com.boyouquan.controller;

import com.boyouquan.model.BlogIntimacy;
import com.boyouquan.model.MyFriendLinks;
import com.boyouquan.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-intimacies")
public class BlogIntimacyController {

    @Autowired
    private FriendLinkService friendLinkService;

    @GetMapping("")
    public ResponseEntity<?> getBlogIntimacies(
            @RequestParam("sourceDomainName") String sourceDomainName,
            @RequestParam("targetDomainName") String targetDomainName) {
        BlogIntimacy blogIntimacy = friendLinkService.computeBlogIntimacies(sourceDomainName, targetDomainName);

        return ResponseEntity.ok(blogIntimacy);
    }

    @GetMapping("/{blogDomainName}")
    public ResponseEntity<?> getMyFriendLinks(
            @PathVariable("blogDomainName") String blogDomainName) {
        MyFriendLinks myFriendLinks = friendLinkService.getMyFriendLinks(blogDomainName);

        return ResponseEntity.ok(myFriendLinks);
    }

}
