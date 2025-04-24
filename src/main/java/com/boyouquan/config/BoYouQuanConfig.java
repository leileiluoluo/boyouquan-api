package com.boyouquan.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "boyouquan")
public class BoYouQuanConfig {

    @Value("${boyouquan.email.enable}")
    private Boolean emailEnabled;

    @Value("${boyouquan.gravatar-url}")
    private String gravatarUrl;

    @Value("${boyouquan.ip-info-query-url}")
    private String ipInfoQueryUrl;

    @Value("${boyouquan.domain-whois-info-query.url}")
    private String domainWhoisInfoQueryUrl;

    @Value("${boyouquan.domain-whois-info-query.api-key}")
    private String domainWhoisInfoQueryApiKey;

    @Value("${boyouquan.domains-refuse-to-join}")
    private List<String> domainsRefuseToJoin;

}
