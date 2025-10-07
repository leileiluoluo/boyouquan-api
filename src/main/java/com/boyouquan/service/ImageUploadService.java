package com.boyouquan.service;

import java.io.IOException;

public interface ImageUploadService {

    byte[] getImageBytes(String year, String month, String filename);

    String upload(String fileName, byte[] bytes) throws IOException;

    String getContentType(String year, String month, String filename);

}
