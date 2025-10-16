package com.boyouquan.controller;

import com.boyouquan.service.BlogService;
import com.boyouquan.service.ImageUploadService;
import com.boyouquan.service.PostImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private PostImageService postImageService;
    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/logo/performance.svg", produces = "image/svg+xml")
    public void getPerformanceSvg(
            @RequestParam(name = "domainName", defaultValue = "leileiluoluo.com") String domainName,
            HttpServletResponse response) throws IOException {
        response.setContentType("image/svg+xml;charset=UTF-8");

        ClassPathResource resource = new ClassPathResource("logo/performance-logo.svg");
        String svg = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // get joined years
        String defaultLevel = "L0";
        Integer years = blogService.getJoinYearsByDomainName(domainName);
        if (null != years) {
            defaultLevel = "L" + years;
        }

        svg = svg.replace("{{LEVEL}}", defaultLevel);

        response.getWriter().write(svg);
    }

    @GetMapping(value = "/logo/performance-dark.svg", produces = "image/svg+xml")
    public void getPerformanceDarkSvg(
            @RequestParam(name = "domainName", defaultValue = "leileiluoluo.com") String domainName,
            HttpServletResponse response) throws IOException {
        response.setContentType("image/svg+xml;charset=UTF-8");

        ClassPathResource resource = new ClassPathResource("logo/performance-logo-dark.svg");
        String svg = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // get joined years
        String defaultLevel = "L0";
        Integer years = blogService.getJoinYearsByDomainName(domainName);
        if (null != years) {
            defaultLevel = "L" + years;
        }

        svg = svg.replace("{{LEVEL}}", defaultLevel);

        response.getWriter().write(svg);
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
