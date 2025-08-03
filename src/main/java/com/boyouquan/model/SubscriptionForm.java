package com.boyouquan.model;

import lombok.Data;

@Data
public class SubscriptionForm {

    private String email;
    private Subscription.Type type;

}
