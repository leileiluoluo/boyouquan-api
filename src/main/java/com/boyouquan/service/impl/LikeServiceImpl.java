package com.boyouquan.service.impl;

import com.boyouquan.dao.LikeDaoMapper;
import com.boyouquan.model.Like;
import com.boyouquan.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDaoMapper likeDaoMapper;

    @Override
    public Long countByTypeAndEntityId(Like.Type type, Long entityId) {
        return likeDaoMapper.countByTypeAndEntityId(type, entityId);
    }

    @Override
    public void addLike(Like.Type type, Long entityId) {
        likeDaoMapper.add(type, entityId);
    }

}
