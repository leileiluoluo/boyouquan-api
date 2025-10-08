package com.boyouquan.dao;

import com.boyouquan.model.Like;

public interface LikeDaoMapper {

    Long countByTypeAndEntityId(Like.Type type, Long entityId);

    void add(Like.Type type, Long entityId);

}
