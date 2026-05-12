package com.boyouquan.dao;

import com.boyouquan.model.PostDetail;

public interface PostDetailDaoMapper {

    boolean existsByLink(String link);

    PostDetail getByLink(String link);

    void save(PostDetail postDetail);

    void updateContentByLink(String link, String content);

}
