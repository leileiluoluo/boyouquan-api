package com.boyouquan.service;

import com.boyouquan.model.ImageDownloadResult;

public interface ImageDownloadService {

    ImageDownloadResult downloadImage(String imageURL);

    boolean compressImage(String inputPath, String outputPath, float quality);

    byte[] getImageBytes(String filePath);

    String getContentType(String filePath);

}
