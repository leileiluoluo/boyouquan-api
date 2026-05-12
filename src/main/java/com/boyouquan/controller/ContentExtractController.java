package com.boyouquan.controller;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.ExtractorResult;
import com.boyouquan.service.ArticleExtractorService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content-extractors")
public class ContentExtractController {

    @Autowired
    private PostService postService;
    @Autowired
    private ArticleExtractorService articleExtractorService;

    @GetMapping("")
    public ResponseEntity<?> extractContent(@RequestParam String link) {
        // validation
        boolean exists = postService.existsByLink(link);
        if (!exists) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        String content = articleExtractorService.getContent(link);

        ExtractorResult result = new ExtractorResult();
        result.setContent(content);

        return ResponseEntity.ok(result);
    }

}
