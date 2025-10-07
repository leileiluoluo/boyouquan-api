package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.PostImageDaoMapper;
import com.boyouquan.model.ImageCompressResult;
import com.boyouquan.model.ImageDownloadResult;
import com.boyouquan.model.PostImage;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.service.PostImageService;
import com.boyouquan.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
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
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private PostImageDaoMapper postImageDaoMapper;
    @Autowired
    private ImageDownloadService imageDownloadService;

    @Override
    public String getImageURLByLink(String link) {
        String imageURL = postImageDaoMapper.getImageURLByLink(link);
        if (StringUtils.isBlank(imageURL)) {
            return null;
        }
        return CommonConstants.POST_IMAGES_URL_PREFIX + imageURL;
    }

    @Override
    public boolean existsImageURLByLink(String link) {
        return postImageDaoMapper.existsImageURLByLink(link);
    }

    @Override
    public byte[] getImageBytes(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getImageBytes(boYouQuanConfig.getPostImageStorePath(), filePath);
    }

    @Override
    public String getContentType(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getContentType(boYouQuanConfig.getPostImageStorePath(), filePath);
    }

    @Override
    public void saveOrUpdate(String link, String rawImageURL, ImageDownloadResult result) {
        PostImage postImageStored = postImageDaoMapper.getByLink(link);

        if (null == postImageStored) {
            PostImage postImage = new PostImage();
            setFields(postImage, link, rawImageURL, result);
            postImageDaoMapper.save(postImage);
        } else {
            setFields(postImageStored, link, rawImageURL, result);
            postImageDaoMapper.update(postImageStored);
        }
    }

    private void setFields(PostImage postImage, String link, String rawImageURL, ImageDownloadResult result) {
        ImageCompressResult compressResult = result.getCompressResult();

        postImage.setLink(link);
        postImage.setImageType(result.getImageType());
        postImage.setRawImageURL(rawImageURL);
        postImage.setRawImageSizeKb(compressResult.getOriginalSizeKb());
        postImage.setRawImageWidth(compressResult.getOriginalImageWidth());
        postImage.setRawImageHeight(compressResult.getOriginalImageHeight());
        postImage.setImageSizeKb(compressResult.getSizeKb());
        postImage.setImageWidth(compressResult.getImageWidth());
        postImage.setImageHeight(compressResult.getImageHeight());
        postImage.setYearStr(CommonUtils.getYearStr(new Date()));
        postImage.setMonthStr(CommonUtils.getMonthStr(new Date()));
        postImage.setImageName(result.getImageName());
    }

}
