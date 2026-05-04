package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.service.MiniProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/mini-programs")
public class MiniProgramController {

    @Autowired
    private MiniProgramService miniProgramService;

    @GetMapping("/qr-codes")
    public ResponseEntity<byte[]> image(@RequestParam("blogDomainName") String blogDomainName) {
        byte[] bytes = miniProgramService.getQrCode(blogDomainName, CommonConstants.MINI_PROGRAM_QR_CODE_DEFAULT_SIZE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(bytes);
    }

}
