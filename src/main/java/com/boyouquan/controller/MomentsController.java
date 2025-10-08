package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.service.ImageUploadService;
import com.boyouquan.service.MomentService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/moments")
public class MomentsController {

    @Autowired
    private MomentService momentService;
    @Autowired
    private ImageUploadService imageUploadService;

    @GetMapping("")
    public ResponseEntity<?> listMoments(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Pagination<MomentInfo> momentInfos = momentService.listMomentInfos(page, CommonConstants.DEFAULT_PAGE_SIZE);
        return ResponseEntity.ok(momentInfos);
    }

    @PostMapping
    public ResponseEntity<?> addMoment(
            @RequestParam("blogDomainName") String blogDomainName,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {
        if (null == file || file.isEmpty()) {
            return ResponseUtil.errorResponse(ErrorCode.IMAGE_UPLOAD_FILE_INVALID);
        }

        if (StringUtils.isBlank(blogDomainName)
                || StringUtils.isBlank(description)) {
            return ResponseUtil.errorResponse(ErrorCode.MOMENTS_PARAMS_INVALID);
        }

        try {
            // upload file
            String imageURL = imageUploadService.upload(file.getOriginalFilename(), file.getBytes());

            Moment moment = new Moment();
            moment.setBlogDomainName(blogDomainName);
            moment.setDescription(description);
            moment.setImageURL(imageURL);
            momentService.save(moment);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseUtil.errorResponse(ErrorCode.IMAGE_UPLOAD_FAILED, e.getMessage());
        }
    }

}
