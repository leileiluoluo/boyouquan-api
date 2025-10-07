package com.boyouquan.controller;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.ImageUploadResult;
import com.boyouquan.service.ImageUploadService;
import com.boyouquan.service.PostImageService;
import com.boyouquan.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private PostImageService postImageService;

    @PostMapping("/uploads")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseUtil.errorResponse(ErrorCode.IMAGE_UPLOAD_INVALID);
        }

        try {
            String imageURL = imageUploadService.upload(file.getOriginalFilename(), file.getBytes());

            ImageUploadResult result = new ImageUploadResult();
            result.setImageURL(imageURL);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseUtil.errorResponse(ErrorCode.IMAGE_UPLOAD_FAILED, e.getMessage());
        }
    }

    @GetMapping("/uploads/{year}/{month}/{filename}")
    public ResponseEntity<byte[]> getUploadedImage(@PathVariable String year, @PathVariable String month, @PathVariable String filename) {
        byte[] imageBytes = imageUploadService.getImageBytes(year, month, filename);

        String contentType = imageUploadService.getContentType(year, month, filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + filename + "\"")
                .body(imageBytes);
    }

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
