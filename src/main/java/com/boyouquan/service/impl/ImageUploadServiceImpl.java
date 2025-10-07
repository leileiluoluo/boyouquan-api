package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.ImageCompressResult;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.service.ImageUploadService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.ImageFileUtils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadServiceImpl.class);

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private ImageDownloadService imageDownloadService;

    @PostConstruct
    public void init() {
        String imageStorePath = boYouQuanConfig.getImageUploadStorePath();
        try {
            Path path = Paths.get(imageStorePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                LOGGER.info("upload path not exists, now create it, path: {}", path);
            }
        } catch (Exception e) {
            LOGGER.error("init failed: {}", imageStorePath, e);
        }
    }

    @Override
    public byte[] getImageBytes(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getImageBytes(boYouQuanConfig.getImageUploadStorePath(), filePath);
    }

    @Override
    public String upload(String fileName, byte[] bytes) throws IOException {
        String fileExtension = ImageFileUtils.getFileExtension(fileName);
        fileName = ImageFileUtils.generateFileName(fileExtension);
        String yearStr = CommonUtils.getYearStr(new Date());
        String monthStr = CommonUtils.getMonthStr(new Date());
        Path outputPath = Paths.get(boYouQuanConfig.getImageUploadStorePath(), yearStr, monthStr);
        Path outputFile = Paths.get(outputPath.toString(), fileName);
        Files.createDirectories(outputPath.getParent());

        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            LOGGER.info("outputPath created, outputPath: {}", outputPath);
        }

        Files.write(outputFile, bytes);

        ImageCompressResult compressResult = ImageFileUtils.compressToNewFile(outputPath.toString(), outputFile, bytes.length, fileExtension);
        if (!compressResult.isSuccess()) {
            LOGGER.error("compress failed");
            throw new IOException("compress failed");
        }

        return String.format("/images/uploads/%s/%s/%s", yearStr, monthStr, compressResult.getFileName());
    }

    @Override
    public String getContentType(String year, String month, String filename) {
        String filePath = year + File.separator + month + File.separator + filename;
        return imageDownloadService.getContentType(boYouQuanConfig.getImageUploadStorePath(), filePath);
    }

}
