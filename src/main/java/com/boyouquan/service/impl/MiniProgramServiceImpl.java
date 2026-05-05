package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.MiniProgramQrCodeBody;
import com.boyouquan.model.MiniProgramToken;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.MiniProgramService;
import com.boyouquan.util.ObjectUtil;
import com.boyouquan.util.OkHttpUtil;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MiniProgramServiceImpl implements MiniProgramService {

    private final Logger logger = LoggerFactory.getLogger(MiniProgramServiceImpl.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    @Autowired
    private BlogService blogService;

    @Override
    public byte[] getQrCode(String blogDomainName, int size) {
        boolean exists = blogService.existsByDomainName(blogDomainName);
        if (!exists) {
            return new byte[]{};
        }

        logger.info("get qr code for {}", blogDomainName);

        try {
            if (existsInLocalStore(blogDomainName, size)) {
                return getFromLocalStore(blogDomainName, size);
            }

            byte[] bytes = getQrCodeFromSource(blogDomainName, size);

            if (bytes.length == 0) {
                logger.error("get empty qr code, blogDomainName: {}", blogDomainName);
                return new byte[]{};
            }

            // write to local store
            writeToLocalStore(blogDomainName, size, bytes);

            return bytes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    private byte[] getFromLocalStore(String blogDomainName, int size) {
        Path filePath = getQrCodeFilePath(blogDomainName, size);

        try {
            if (existsInLocalStore(blogDomainName, size)) {
                return Files.readAllBytes(filePath);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return new byte[]{};
    }

    private void writeToLocalStore(String blogDomainName, int size, byte[] bytes) {
        Path folderPath = getQrCodeFolderPath(blogDomainName);
        Path filePath = getQrCodeFilePath(blogDomainName, size);

        try {
            Files.createDirectories(folderPath);
            Files.write(filePath, bytes);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private byte[] getQrCodeFromSource(String blogDomainName, int size) {
        String token = getToken();
        if (StringUtils.isBlank(token)) {
            return new byte[]{};
        }

        return getQrCodeFromSource(blogDomainName, token, size);
    }

    private String getToken() {
        String appId = boYouQuanConfig.getMiniProgramAppId();
        String appSecret = boYouQuanConfig.getMiniProgramAppSecret();
        String tokenUrl = String.format(boYouQuanConfig.getMiniProgramTokenUrl(), appId, appSecret);

        Request request = new Request.Builder()
                .url(tokenUrl)
                .build();

        try (Response response = client.newCall(request).execute();
             ResponseBody body = response.body()) {

            if (HttpStatus.OK.value() != response.code()
                    || !response.isSuccessful()) {
                logger.error("request mini program token failed, code: {}", response.code());
                return "";
            }

            // result
            String jsonResult = body.string();
            logger.info("jsonResult: {}", jsonResult);
            MiniProgramToken token = ObjectUtil.jsonToObject(jsonResult, MiniProgramToken.class);
            if (null == token) {
                logger.error("request mini program token failed, body: {}", jsonResult);
                return "";
            }

            return token.getAccessToken();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    private byte[] getQrCodeFromSource(String blogDomainName, String token, int size) {
        String qrCodeUrl = String.format(boYouQuanConfig.getMiniProgramQrCodeUrl(), token);

        String path = String.format(CommonConstants.MINI_PROGRAM_BLOG_PAGE_PATH, blogDomainName);
        MiniProgramQrCodeBody qrCodeBody = MiniProgramQrCodeBody
                .builder()
                .path(path)
                .width(size)
                .build();

        RequestBody requestBody = RequestBody.create(
                ObjectUtil.objectToJson(qrCodeBody),
                MediaType.parse(CommonConstants.COMMON_JSON_BODY_MEDIA_TYPE)
        );

        Request request = new Request.Builder()
                .url(qrCodeUrl)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute();
             ResponseBody body = response.body()) {

            if (HttpStatus.OK.value() != response.code()
                    || !response.isSuccessful()) {
                logger.error("request mini program qr code failed, code: {}", response.code());
                return new byte[]{};
            }

            byte[] bytes = body.bytes();
            logger.info("bytes length: {}", bytes.length);

            return bytes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    private boolean existsInLocalStore(String blogDomainName, int size) {
        return Files.exists(getQrCodeFilePath(blogDomainName, size));
    }

    private Path getQrCodeFolderPath(String blogDomainName) {
        String localPath = String.format(CommonConstants.MINI_PROGRAM_STORE_FOLDER, blogDomainName);
        return Path.of(localPath);
    }

    private Path getQrCodeFilePath(String blogDomainName, int size) {
        String localPath = String.format(CommonConstants.MINI_PROGRAM_STORE_FOLDER, blogDomainName);
        return Path.of(localPath, String.format("%d.jpg", size));
    }

}
