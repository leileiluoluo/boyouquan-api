package com.boyouquan.model;

import lombok.Data;

@Data
public class CancelSubscriptionForm {

    private String email;
    private Subscription.Type type;

}
