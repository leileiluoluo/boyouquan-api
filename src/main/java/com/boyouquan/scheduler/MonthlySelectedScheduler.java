package com.boyouquan.scheduler;

import com.boyouquan.model.Subscription;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonthlySelectedScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlySelectedScheduler.class);

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private MonthlySelectedService monthlySelectedService;

    @Scheduled(cron = "0 0 8 1 * ?")
    public void sendPosts() {
        LOGGER.info("monthly selected scheduler start!");

        List<Subscription> subscriptions = subscriptionService.listSubscribedByType(Subscription.Type.MONTHLY_SELECTED);
        for (Subscription subscription : subscriptions) {
            String email = subscription.getEmail();
            monthlySelectedService.sendLatestReport(email);
        }

        LOGGER.info("monthly selected scheduler end!");
    }

}
