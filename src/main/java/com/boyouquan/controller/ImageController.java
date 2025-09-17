//package com.boyouquan.controller;
//
//import com.boyouquan.constant.CommonConstants;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/images")
//public class ImageController {
//
//    @GetMapping("/{filename}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
//        try {
//            byte[] imageBytes = imageService.getImageBytes(filename);
//            ImageEntity imageInfo = imageService.getImageInfo(filename);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(imageInfo.getContentType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "inline; filename=\"" + imageInfo.getOriginalFilename() + "\"")
//                    .body(imageBytes);
//        } catch (IOException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//}
