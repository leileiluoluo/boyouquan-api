package com.boyouquan.dao;

import com.boyouquan.model.PostImage;

public interface PostImageDaoMapper {

    PostImage getByLink(String link);

    String getImageURLByLink(String link);

    boolean existsImageURLByLink(String link);

    void save(PostImage postImage);

    void update(PostImage postImage);

}
