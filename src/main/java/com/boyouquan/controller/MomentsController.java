package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.service.MomentService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moments")
public class MomentsController {

    @Autowired
    private MomentService momentService;

    @GetMapping("")
    public ResponseEntity<?> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Pagination<MomentInfo> momentInfos = momentService.listMomentInfos(page, CommonConstants.DEFAULT_PAGE_SIZE);
        return ResponseEntity.ok(momentInfos);
    }

    @PostMapping
    public void addMoment(@RequestBody Moment moment) {
        momentService.save(moment);
    }

}
