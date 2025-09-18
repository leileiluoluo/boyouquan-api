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
        String imageURL = "https://macinorg-blog.oss-cn-chengdu.aliyuncs.com/blog/Shirley_IMG_5592.webp?x-oss-process=style/wechat-mp";
        imageDownloadService.downloadImage(imageURL);
    }

}
