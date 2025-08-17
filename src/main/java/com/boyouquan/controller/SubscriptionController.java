package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.EncryptionHelper;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.CancelSubscriptionForm;
import com.boyouquan.model.Subscription;
import com.boyouquan.model.SubscriptionForm;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.service.SubscriptionService;
import com.boyouquan.util.IPUtil;
import com.boyouquan.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private EncryptionHelper encryptionHelper;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private MonthlySelectedService monthlySelectedService;
    @Autowired
    private IPControlHelper ipControlHelper;

    @GetMapping("/{email}")
    public ResponseEntity<?> listSubscriptions(@PathVariable("email") String email) {
        List<Subscription> subscriptions = subscriptionService.listSubscribedByEmail(email);
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping("")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionForm subscriptionForm, HttpServletRequest request) {
        String ip = IPUtil.getRealIp(request);

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

        Integer count = ipControlHelper.getSubscriptionCount(ip, subscriptionForm.getType().name());
        if (count >= CommonConstants.SAME_IP_SUBSCRIPTION_COUNT_LIMIT) {
            return ResponseUtil.errorResponse(ErrorCode.SUBSCRIPTION_IP_COUNT_LIMIT_EXCEED);
        }

        // subscribe
        subscriptionService.subscribe(subscriptionForm.getEmail(), subscriptionForm.getType());
        ipControlHelper.subscribe(ip, subscriptionForm.getType().name());

        // send email
        executorService.execute(() -> {
            // send email
            if (Subscription.Type.MONTHLY_SELECTED.equals(subscriptionForm.getType())) {
                monthlySelectedService.sendLatestReport(subscriptionForm.getEmail());
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> unsubscribe(@RequestBody CancelSubscriptionForm form) {
        // business validation
        if (!encryptionHelper.encrypt(form.getEmail()).equals(form.getToken())) {
            return ResponseUtil.errorResponse(ErrorCode.SUBSCRIPTION_CANCEL_TOKEN_INVALID);
        }

        boolean exists = subscriptionService.existsSubscribedByEmail(form.getEmail());
        if (!exists) {
            return ResponseUtil.errorResponse(ErrorCode.SUBSCRIPTION_NOT_EXISTS);
        }

        // unsubscribe
        for (Subscription.Type type : form.getTypes()) {
            subscriptionService.unsubscribe(form.getEmail(), type);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
