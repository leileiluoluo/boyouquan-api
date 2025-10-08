package com.boyouquan.controller;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.Like;
import com.boyouquan.service.LikeService;
import com.boyouquan.util.IPUtil;
import com.boyouquan.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/likes")
public class LikeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private IPControlHelper ipControlHelper;
    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<?> like(@RequestBody Like like, HttpServletRequest request) {
        String ip = IPUtil.getRealIp(request);
        LOGGER.info("someone clicked the likes, ip: {}", ip);

        if (ipControlHelper.alreadyLiked(ip, like.getType(), like.getEntityId())) {
            return ResponseUtil.errorResponse(ErrorCode.LIKE_LIMIT_EXCEEDED);
        }

        likeService.addLike(like.getType(), like.getEntityId());
        ipControlHelper.like(ip, like.getType(), like.getEntityId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
