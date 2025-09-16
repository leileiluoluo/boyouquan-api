package com.boyouquan.service;

import com.boyouquan.model.PostImage;

public interface PostImageService {

    String getImageURLByLink(String link);

    boolean existsImageURLByLink(String link);

    void save(PostImage postImage);

}
