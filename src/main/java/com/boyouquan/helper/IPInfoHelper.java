package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.BlogLocation;
import com.boyouquan.model.IPLocationInfo;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.IPUtil;
import com.boyouquan.util.ObjectUtil;
import com.boyouquan.util.OkHttpUtil;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IPInfoHelper {

    private static final Logger logger = LoggerFactory.getLogger(IPInfoHelper.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    public BlogLocation getIpInfoByDomainName(String domainName) {
        String domain = CommonUtils.getDomainFromBlogDomainName(domainName);
        String ip = IPUtil.domainToIp(domain);
        if (StringUtils.isBlank(ip)) {
            return null;
        }

        String url = String.format(boYouQuanConfig.getIpInfoQueryUrl(), ip);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute();
             ResponseBody responseBody = response.body()) {
            String body = responseBody.string();
            if (HttpStatus.OK.value() != response.code()) {
                logger.error("request ip info failed, status: {}, body: {}", response.code(), body);
            }

            IPLocationInfo ipLocationInfo = ObjectUtil.jsonToObject(body, IPLocationInfo.class);

            BlogLocation blogLocation = new BlogLocation();
            blogLocation.setDomainName(domainName);
            blogLocation.setLocation(ipLocationInfo.getLocationInfo());

            logger.info("domain: {}, ip: {}, location: {}", domain, ip, ipLocationInfo.getLocationInfo());

            return blogLocation;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
