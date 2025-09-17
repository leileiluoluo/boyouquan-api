package com.boyouquan.dao;

import com.boyouquan.model.PostImage;

public interface PostImageDaoMapper {

    String getImageURLByLink(String link);

    boolean existsImageURLByLink(String link);

    void save(PostImage postImage);

    void updateImageURL(String link, String imageURL);

}
