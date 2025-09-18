package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.ImageCompressResult;
import com.boyouquan.model.ImageDownloadResult;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.OkHttpUtil;
import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

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
        } catch (IOException e) {
            LOGGER.error("download path create failed: {}", imageStorePath, e);
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
        try (Response resp = client.newCall(request).execute()) {
            ResponseBody body = resp.body();
            if (!resp.isSuccessful()) {
                LOGGER.error("failed to download file, code: {}, body: {}", resp.code(), body.string());
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("failed to download file")
                        .build();
            }

            fileExtension = getFileExtension(imageURL);
            if (StringUtils.isBlank(fileExtension)) {
                LOGGER.error("file extension unknown, imageURL: {}", imageURL);
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("file extension unknown")
                        .build();
            }

            String fileName = generateFileName(fileExtension);
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

        String finalFilePath = outputFile.toString();
        ImageCompressResult compressResult = compressToNewFile(outputPath.toString(), outputFile, totalBytes, fileExtension);
        if (compressResult.isSuccess()) {
            finalFilePath = compressResult.getFilePath();
        }

        return ImageDownloadResult.builder()
                .success(true)
                .filePath(finalFilePath)
                .totalBytes(totalBytes)
                .imageType(fileExtension)
                .build();
    }

    private long compressImage(String inputPath, String outputPath, int width, int height, float quality) {
        try {
            File outputFile = new File(outputPath);
            Thumbnails.of(new File(inputPath))
                    .size(width, height)
                    .outputQuality(quality)
                    .toFile(outputFile);

            return outputFile.length();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public byte[] getImageBytes(String filePath) {
        Path fileFullPath = Paths.get(boYouQuanConfig.getPostImageStorePath(), filePath);
        try {
            return Files.readAllBytes(fileFullPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    @Override
    public String getContentType(String filePath) {
        Path fileFullPath = Paths.get(boYouQuanConfig.getPostImageStorePath(), filePath);
        try {
            return Files.probeContentType(fileFullPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private String getFileExtension(String imageURL) {
        if (imageURL.contains(".")) {
            String urlPath = imageURL.split("\\?")[0];
            String[] parts = urlPath.split("\\.");
            if (parts.length > 1) {
                String potentialExt = "." + parts[parts.length - 1].toLowerCase();
                if (potentialExt.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                    return potentialExt;
                }
            }
        }
        return null;
    }

    private String generateFileName(String extension) {
        return "image_" + UUID.randomUUID().toString().substring(0, 8) + extension;
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

    private ImageCompressResult compressToNewFile(String outputPath, Path sourceFile, long originalTotalBytes, String fileExtension) {
        try {
            String newFileName = generateFileName(fileExtension);
            Path newOutputFile = Paths.get(outputPath, newFileName);
            int originalImageWidth = getImageWidth(sourceFile.toString());
            int originalImageHeight = getImageHeight(sourceFile.toString());
            int imageWidth = originalImageWidth;
            int imageHeight = originalImageHeight;
            long totalBytes = 0L;
            if (originalTotalBytes / 1000 > CommonConstants.POST_IMAGES_SIZE_LIMIT) {
                if (originalImageWidth > CommonConstants.POST_IMAGES_WIDTH_LIMIT) {
                    imageWidth = CommonConstants.POST_IMAGES_WIDTH_LIMIT;
                    imageHeight = originalImageHeight * CommonConstants.POST_IMAGES_WIDTH_LIMIT / originalImageWidth;
                }

                totalBytes = compressImage(sourceFile.toString(), newOutputFile.toString(), imageWidth, imageHeight, 1);
                if (totalBytes > 0L) {

                    Files.delete(sourceFile);
                    LOGGER.info("original file has been deleted, file: {}", sourceFile);

                    return ImageCompressResult.builder()
                            .success(true)
                            .originalFilePath(sourceFile.toString())
                            .originalSizeKb(totalBytes / 1000)
                            .originalImageWidth(originalImageWidth)
                            .originalImageHeight(originalImageHeight)
                            .filePath(newOutputFile.toString())
                            .sizeKb(totalBytes / 100)
                            .imageWidth(imageWidth)
                            .imageHeight(imageHeight)
                            .build();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ImageCompressResult.builder().success(false).build();
    }

    private int getImageWidth(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            return image.getWidth();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }

    private int getImageHeight(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            return image.getHeight();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }

}
