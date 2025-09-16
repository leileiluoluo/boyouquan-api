package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.service.ImageScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post-images")
public class PostImageController {

    @Autowired
    private ImageScraperService imageScraperService;

    @GetMapping("/raw-images/by-link")
    public ResponseEntity<?> getRawImagesByLink(@RequestParam("link") String link) {
        List<String> imageURLs = imageScraperService.getImages(link, CommonConstants.POST_IMAGES_SCRAPER_SIZE);

        return ResponseEntity.ok(imageURLs);
    }

}
