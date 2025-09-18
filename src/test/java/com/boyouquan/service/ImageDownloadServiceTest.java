package com.boyouquan.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageDownloadServiceTest {

    @Autowired
    private ImageDownloadService imageDownloadService;

    @Test
    public void testDownloadImage() {
        String imageURL = "https://www.boyouquan.com/images/2025/09/image_67989cbe.jpg";
        imageDownloadService.downloadImage(imageURL);
    }

}
