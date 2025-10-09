package com.boyouquan.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String sourceIp = checkIp(ip) ? ip : (
                checkIp(ip = request.getHeader("Proxy-Client-IP")) ? ip : (
                        checkIp(ip = request.getHeader("WL-Proxy-Client-IP")) ? ip :
                                request.getRemoteAddr()));

        String realIp = extractRealIp(sourceIp);
        logger.info("sourceIp: {}, realIp: {}", sourceIp, realIp);

        return realIp;
    }

    private static boolean checkIp(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    public static String domainToIp(String domain) {
        try {
            InetAddress inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    // FIXME: when use CDN, the IP will be a pair like "113.226.141.95, 125.94.248.153",
    //  we need to use below method to extract the real IP
    private static String extractRealIp(String ip) {
        if (StringUtils.isNotBlank(ip) && ip.contains(", ")) {
            String[] pairs = ip.split(", ");
            if (pairs.length > 0) {
                return pairs[0];
            }
        }
        return ip;
    }

}