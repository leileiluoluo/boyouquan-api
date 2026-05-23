package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.service.GravatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/gravatar")
public class GravatarController {

    @Autowired
    private GravatarService gravatarService;

    @GetMapping("/{md5Email}")
    public ResponseEntity<?> image(@PathVariable("md5Email") String md5Email, @RequestParam("size") int size) {
        List<Integer> supportedSizes = List.of(
                CommonConstants.GRAVATAR_IMAGE_SMALL_SIZE,
                CommonConstants.GRAVATAR_IMAGE_LARGE_SIZE,
                CommonConstants.GRAVATAR_IMAGE_MEDIUM_SIZE
        );

        if (!supportedSizes.contains(size)) {
            return ResponseEntity.ok(new byte[]{});
        }

        byte[] bytes = gravatarService.getImage(md5Email, size);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(bytes);
    }

}
