package com.boyouquan.model;

import lombok.*;

import java.util.Date;

@Data
public class Subscription {

    private String email;
    private Type type;
    private Date subscribedAt;
    private Date unsubscribedAt;
    private Boolean unsubscribed = false;

    public enum Type {
        MONTHLY_SELECTED,
        ALL
    }

}
