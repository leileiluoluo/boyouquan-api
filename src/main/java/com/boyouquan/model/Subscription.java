package com.boyouquan.model;

import lombok.*;

import java.util.Date;

@Data
public class Subscription {

    private String email;
    private Type type;
    private String name;
    private Date subscribedAt;
    private Date unsubscribedAt;
    private Boolean unsubscribed = false;

    public enum Type {
        MONTHLY_SELECTED("MONTHLY_SELECTED", "每月精选"),
        ALL("ALL", "所有频道");

        private final String key;
        private final String value;

        Type(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static Type fromKey(String key) {
            for (Type type : Type.values()) {
                if (type.key.equals(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    public String getName() {
        if (null == this.type) {
            return null;
        }
        return type.value;
    }

}
