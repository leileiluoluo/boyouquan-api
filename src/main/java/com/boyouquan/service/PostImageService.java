package com.boyouquan.service;

public interface PostImageService {

    String getImageURLByLink(String link);

    boolean existsImageURLByLink(String link);

    byte[] getImageBytes(String year, String month, String filename);

    String getContentType(String year, String month, String filename);

    void saveOrUpdate(String link, String imageURL);

}
