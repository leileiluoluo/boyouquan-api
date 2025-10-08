package com.boyouquan.controller;

import com.boyouquan.model.Like;
import com.boyouquan.service.LikeService;
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

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<?> like(@RequestBody Like like) {
        likeService.addLike(like.getType(), like.getEntityId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
