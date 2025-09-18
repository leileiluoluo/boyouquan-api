package com.boyouquan.service.impl;

import com.boyouquan.dao.PostImageDaoMapper;
import com.boyouquan.model.ImageDownloadResult;
import com.boyouquan.model.PostImage;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.service.PostImageService;
import com.boyouquan.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
public class PostImageServiceImpl implements PostImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostImageServiceImpl.class);

    @Autowired
    private PostImageDaoMapper postImageDaoMapper;
    @Autowired
    private ImageDownloadService imageDownloadService;

    @Override
    public String getImageURLByLink(String link) {
        PostImage postImage = postImageDaoMapper.getByLink(link);
        if (null == postImage) {
            return null;
        }
        return "/images/" + postImage.getYearStr() + "/" + postImage.getMonthStr() + "/" + postImage.getImageURL();
    }

    @Override
    public boolean existsImageURLByLink(String link) {
        return postImageDaoMapper.existsImageURLByLink(link);
    }

    @Override
    public byte[] getImageBytes(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getImageBytes(filePath);
    }

    @Override
    public String getContentType(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getContentType(filePath);
    }

    @Override
    public void saveOrUpdate(String link, String rawImageURL, ImageDownloadResult result) {
        PostImage postImageStored = postImageDaoMapper.getByLink(link);
        if (null == postImageStored) {
            PostImage postImage = new PostImage();
            postImage.setLink(link);
            postImage.setRawImageURL(rawImageURL);
            postImage.setImageURL(result.getFilePath());
            postImage.setImageType(result.getImageType());
            postImage.setImageSizeKb(result.getTotalBytes() / 1000);
            postImage.setYearStr(CommonUtils.getYearStr(new Date()));
            postImage.setMonthStr(CommonUtils.getMonthStr(new Date()));
            postImageDaoMapper.save(postImage);
        } else {
            postImageStored.setRawImageURL(rawImageURL);
            postImageStored.setImageURL(result.getFilePath());
            postImageStored.setImageType(result.getImageType());
            postImageStored.setImageSizeKb(result.getTotalBytes() / 1000);
            postImageStored.setYearStr(CommonUtils.getYearStr(new Date()));
            postImageStored.setMonthStr(CommonUtils.getMonthStr(new Date()));
            postImageDaoMapper.update(postImageStored);
        }
    }

}
