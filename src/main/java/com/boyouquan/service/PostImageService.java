package com.boyouquan.service;

public interface PostImageService {

    String getImageURLByLink(String link);

    boolean existsImageURLByLink(String link);

    void saveOrUpdate(String link, String imageURL);

}
