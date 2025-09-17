package com.boyouquan.dao;

import com.boyouquan.model.PostImage;

public interface PostImageDaoMapper {

    String getImageURLByLink(String link);

    PostImage getByLink(String link);

    boolean existsImageURLByLink(String link);

    void save(PostImage postImage);

    void update(PostImage postImage);

}
