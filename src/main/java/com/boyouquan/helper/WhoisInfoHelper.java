package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.WhoisInfo;
import com.boyouquan.util.ObjectUtil;
import com.boyouquan.util.OkHttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WhoisInfoHelper {

    private static final Logger logger = LoggerFactory.getLogger(WhoisInfoHelper.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    public WhoisInfo getDomainNameInfoByRealDomainName(String realDomainName) {
        String url = String.format(boYouQuanConfig.getDomainWhoisInfoQueryUrl(), realDomainName);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", boYouQuanConfig.getDomainWhoisInfoQueryApiKey())
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute(); ResponseBody responseBody = response.body()) {
            String body = responseBody.string();
            if (HttpStatus.OK.value() != response.code()) {
                logger.error("request whois info failed, status: {}, body: {}", response.code(), body);
                return null;
            }

            WhoisInfo whoisInfo = ObjectUtil.jsonToObject(body, WhoisInfo.class);
            if (null == whoisInfo.getCreated()) {
                logger.error("request whois info failed, body: {}", body);
                return null;
            }
            return whoisInfo;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
