package com.boyouquan.controller;

import com.boyouquan.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private PostImageService postImageService;

    @GetMapping("/{year}/{month}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String year, @PathVariable String month, @PathVariable String filename) {
        byte[] imageBytes = postImageService.getImageBytes(year, month, filename);

        String contentType = postImageService.getContentType(year, month, filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + filename + "\"")
                .body(imageBytes);
    }

}
