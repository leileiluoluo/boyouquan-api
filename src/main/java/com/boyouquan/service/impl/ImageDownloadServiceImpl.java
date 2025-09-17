package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.ImageDownloadResult;
import com.boyouquan.service.ImageDownloadService;
import com.boyouquan.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageDownloadServiceImpl implements ImageDownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadServiceImpl.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Override
    public ImageDownloadResult downloadImage(String imageURL, String outputPath) {
        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(imageURL)
                .build();

        try (Response resp = client.newCall(request).execute()) {
            ResponseBody body = resp.body();
            if (!resp.isSuccessful()) {
                LOGGER.error("failed to download file, code: {}, body: {}", resp.code(), body.string());
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("failed to download file")
                        .build();
            }

            String fileExtension = getFileExtension(imageURL);
            if (StringUtils.isBlank(fileExtension)) {
                LOGGER.error("file extension unknown, imageURL: {}", imageURL);
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("file extension unknown")
                        .build();
            }

            String fileName = generateFileName(fileExtension);
            Path outputFile = Paths.get(outputPath, fileName);

            long contentLength = body.contentLength();
            if (contentLength == -1) {
                LOGGER.error("content length is zero, imageURL: {}", imageURL);
                return ImageDownloadResult.builder()
                        .success(false)
                        .message("content length is zero")
                        .build();
            }

            try (InputStream inputStream = body.byteStream()) {
                try (var outputStream = new FileOutputStream(outputFile.toFile())) {
                    byte[] buffer = new byte[81920];
                    int bytesRead;
                    long totalBytes = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }

                    return ImageDownloadResult.builder()
                            .success(true)
                            .filePath(outputFile.getFileName().toString())
                            .totalBytes(totalBytes)
                            .imageType(fileExtension)
                            .build();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ImageDownloadResult.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
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

}
