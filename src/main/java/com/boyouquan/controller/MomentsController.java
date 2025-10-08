package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.service.MomentService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moments")
public class MomentsController {

    @Autowired
    private MomentService momentService;

    @GetMapping("")
    public ResponseEntity<?> listMoments(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Pagination<MomentInfo> momentInfos = momentService.listMomentInfos(page, CommonConstants.DEFAULT_PAGE_SIZE);
        return ResponseEntity.ok(momentInfos);
    }

    @PostMapping
    public ResponseEntity<?> addMoment(@RequestBody Moment moment) {
        if (null == moment
                || StringUtils.isBlank(moment.getBlogDomainName())
                || StringUtils.isBlank(moment.getDescription())
                || StringUtils.isBlank(moment.getImageURL())) {
            return ResponseUtil.errorResponse(ErrorCode.MOMENTS_PARAMS_INVALID);
        }

        momentService.save(moment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
