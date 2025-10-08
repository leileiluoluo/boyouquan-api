package com.boyouquan.service;

import com.boyouquan.model.Like;

public interface LikeService {

    Long countByTypeAndEntityId(Like.Type type, Long entityId);

    void addLike(Like.Type type, Long entityId);

}
