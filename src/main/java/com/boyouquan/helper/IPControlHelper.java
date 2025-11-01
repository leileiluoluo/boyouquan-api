package com.boyouquan.helper;

import com.boyouquan.model.Like;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IPControlHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPControlHelper.class);

    private static final Map<String, Integer> ACCESS_MAP = new HashMap<>();

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearMap() {
        LOGGER.info("prepare to clear access map, keys: {}", ACCESS_MAP.keySet().size());

        ACCESS_MAP.clear();

        LOGGER.info("access map cleared!");
    }

    public boolean alreadyAccessed(String ip, String link) {
        String key = ip + "#" + link;
        return ACCESS_MAP.containsKey(key);
    }

    public void access(String ip, String link) {
        String key = ip + "#" + link;
        ACCESS_MAP.put(key, 1);
    }

    public boolean alreadyPublishBroadcast(String ip, String messageType) {
        String key = ip + "#broadcase#" + messageType;
        return ACCESS_MAP.containsKey(key);
    }

    public void publishBroadcast(String ip, String messageType) {
        String key = ip + "#broadcase#" + messageType;
        ACCESS_MAP.put(key, 1);
    }

    public boolean alreadyLiked(String ip, Like.Type type, Long id) {
        String key = ip + "#" + type + "#" + id;
        return ACCESS_MAP.containsKey(key);
    }

    public void like(String ip, Like.Type type, Long id) {
        String key = ip + "#" + type + "#" + id;
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
