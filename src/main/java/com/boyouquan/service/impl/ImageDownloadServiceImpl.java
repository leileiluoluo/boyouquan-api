package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.ImageCompressResult;
import com.boyouquan.model.ImageDownloadResult;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.ImageFileUtils;
import com.boyouquan.util.OkHttpUtil;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ImageDownloadServiceImpl implements ImageDownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadServiceImpl.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    @PostConstruct
    public void init() {
        String imageStorePath = boYouQuanConfig.getPostImageStorePath();
        try {
            Path path = Paths.get(imageStorePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                LOGGER.info("download path not exists, now create it, path: {}", path);
            }

            // FIXME: register webp support
            ImageIO.scanForPlugins();
        } catch (Exception e) {
            LOGGER.error("init failed: {}", imageStorePath, e);
        }
    }

    @Override
    public ImageDownloadResult downloadImage(String imageURL) {
        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(imageURL)
                .build();

        long totalBytes = 0L;
        String fileExtension = null;
        Path outputPath = null;
        Path outputFile = null;
        String fileName = null;
        try (Response resp = client.newCall(request).execute()) {
            ResponseBody body = resp.body();
            if (!resp.isSuccessful()) {
                LOGGER.error("failed to download file, code: {}, body: {}", resp.code(), body.string());
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("failed to download file")
                        .build();
            }

            fileExtension = ImageFileUtils.getFileExtension(imageURL);
            if (StringUtils.isBlank(fileExtension)) {
                LOGGER.error("file extension unknown, imageURL: {}", imageURL);
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("file extension unknown")
                        .build();
            }

            // FIXME: special type
            if (".webp".equalsIgnoreCase(fileExtension)) {
                List<String> supportedVersions = Arrays.asList(ImageIO.getReaderFormatNames());
                if (!supportedVersions.contains("webp")) {
                    LOGGER.error("there is no webp plugin, imageURL: {}", imageURL);
                    return ImageDownloadResult.builder()
                            .success(false)
                            .message("there is no webp plugin")
                            .build();
                }
            }

            fileName = ImageFileUtils.generateFileName(fileExtension);
            String yearStr = CommonUtils.getYearStr(new Date());
            String monthStr = CommonUtils.getMonthStr(new Date());
            outputPath = Paths.get(boYouQuanConfig.getPostImageStorePath(), yearStr, monthStr);
            outputFile = Paths.get(outputPath.toString(), fileName);

            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                LOGGER.info("outputPath created, outputPath: {}", outputPath);
            }

            totalBytes = writeFile(outputFile, body);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ImageDownloadResult.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }

        if (totalBytes <= 0) {
            LOGGER.error("totalBytes is zero, imageURL: {}", imageURL);
            return ImageDownloadResult.builder()
                    .success(false)
                    .message("totalBytes is zero")
                    .build();
        }

        ImageCompressResult compressResult = ImageFileUtils.compressToNewFile(outputPath.toString(), outputFile, totalBytes, fileExtension);
        if (!compressResult.isSuccess()) {
            return ImageDownloadResult.builder()
                    .success(false)
                    .message("compress failed")
                    .build();
        }

        return ImageDownloadResult.builder()
                .success(true)
                .imageName(compressResult.getFileName())
                .totalBytes(totalBytes)
                .imageType(fileExtension)
                .compressResult(compressResult)
                .build();
    }

    @Override
    public byte[] getImageBytes(String basePath, String filePath) {
        Path fileFullPath = Paths.get(basePath, filePath);
        try {
            return Files.readAllBytes(fileFullPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    @Override
    public String getContentType(String basePath, String filePath) {
        Path fileFullPath = Paths.get(basePath, filePath);
        try {
            return Files.probeContentType(fileFullPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private long writeFile(Path outputFile, ResponseBody body) {
        long totalBytes = 0;

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(outputFile.toFile());
            byte[] buffer = new byte[81920];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            outputStream.flush();
            outputStream.getFD().sync();

            return totalBytes;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return 0L;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != body) {
                    body.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
