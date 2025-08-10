package com.boyouquan.model;

import lombok.Data;

import java.util.List;

@Data
public class CancelSubscriptionForm {

    private String email;
    private String token;
    private List<Subscription.Type> types;

}
