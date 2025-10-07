package com.boyouquan.service;

import com.boyouquan.model.ImageDownloadResult;

public interface ImageDownloadService {

    ImageDownloadResult downloadImage(String imageURL);

    byte[] getImageBytes(String basePath, String filePath);

    String getContentType(String basePath, String filePath);

}
