package com.boyouquan.service.impl;

import com.boyouquan.dao.PostImageDaoMapper;
import com.boyouquan.model.PostImage;
import com.boyouquan.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostImageServiceImpl implements PostImageService {

    @Autowired
    private PostImageDaoMapper postImageDaoMapper;

    @Override
    public String getImageURLByLink(String link) {
        return postImageDaoMapper.getImageURLByLink(link);
    }

    @Override
    public boolean existsImageURLByLink(String link) {
        return postImageDaoMapper.existsImageURLByLink(link);
    }

    @Override
    public void save(PostImage postImage) {
        postImageDaoMapper.save(postImage);
    }

}
