package com.boyouquan.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class IPControlHelper {

    private final Logger logger = LoggerFactory.getLogger(IPControlHelper.class);

    private static final Map<String, Integer> ACCESS_MAP = new HashMap<>();

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearMap() {
        logger.info("prepare to clear access map, keys: {}", ACCESS_MAP.keySet().size());

        ACCESS_MAP.clear();

        logger.info("access map cleared!");
    }

    public boolean alreadyAccessed(String ip, String link) {
        String key = ip + "#" + link;
        return ACCESS_MAP.containsKey(key);
    }

    public void access(String ip, String link) {
        String key = ip + "#" + link;
        ACCESS_MAP.put(key, 1);
    }

    public Integer getSubscriptionCount(String ip, String type) {
        String key = ip + "#" + type;
        if (!ACCESS_MAP.containsKey(key)) {
            return 0;
        }
        return ACCESS_MAP.get(key);
    }

    public void subscribe(String ip, String type) {
        String key = ip + "#" + type;
        if (!ACCESS_MAP.containsKey(key)) {
            ACCESS_MAP.put(key, 1);
        } else {
            ACCESS_MAP.put(key, ACCESS_MAP.get(key) + 1);
        }
    }

}
