package com.boyouquan.controller;

import com.boyouquan.model.ExtractorResult;
import com.boyouquan.service.ArticleExtractorService;
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
    private ArticleExtractorService articleExtractorService;

    @GetMapping("")
    public ResponseEntity<ExtractorResult> extractContent(@RequestParam String url) {
        String content = articleExtractorService.getContent(url);

        ExtractorResult result = new ExtractorResult();
        result.setContent(content);

        return ResponseEntity.ok(result);
    }

}
