package com.boyouquan.service;

import com.boyouquan.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    List<Subscription> listSubscribedByType(Subscription.Type type);

    List<Subscription> listSubscribedByEmail(String email);

    boolean existsSubscribedByEmailAndType(String email, Subscription.Type type);

    void subscribe(String email, Subscription.Type type);

    void unsubscribe(String email, Subscription.Type type);

}
