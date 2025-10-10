package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/popular-blogs")
public class PopularBlogsController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public ResponseEntity<List<BlogInfo>> list(
            @RequestParam(value = "size", required = false, defaultValue = CommonConstants.POPULAR_BLOGGERS_SIZE) int size) {
        List<BlogInfo> blogInfos = blogService.listPopularBlogInfos(size);
        return ResponseEntity.ok(blogInfos);
    }

}
