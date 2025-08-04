package com.boyouquan.controller;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.Subscription;
import com.boyouquan.model.SubscriptionForm;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.service.SubscriptionService;
import com.boyouquan.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private MonthlySelectedService monthlySelectedService;

    @PostMapping("")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionForm subscriptionForm) {
        // params validation
        if (null == subscriptionForm
                || StringUtils.isBlank(subscriptionForm.getEmail())
                || null == subscriptionForm.getType()) {
            return ResponseUtil.errorResponse(ErrorCode.SUBSCRIPTION_PARAMS_INVALID);
        }

        // business validation
        boolean exists = subscriptionService.existsSubscribedByEmailAndType(subscriptionForm.getEmail(), subscriptionForm.getType());
        if (exists) {
            return ResponseUtil.errorResponse(ErrorCode.SUBSCRIPTION_EXISTS);
        }

        // subscribe
        subscriptionService.subscribe(subscriptionForm.getEmail(), subscriptionForm.getType());

        // send email
        executorService.execute(() -> {
            // send email
            if (Subscription.Type.MONTHLY_SELECTED.equals(subscriptionForm.getType())) {
                monthlySelectedService.sendLatestReport(subscriptionForm.getEmail());
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
