package com.boyouquan.dao;

import com.boyouquan.model.Subscription;

import java.util.List;

public interface SubscriptionDaoMapper {

    List<Subscription> listSubscribedByType(Subscription.Type type);

    boolean existsSubscribedByEmailAndType(String email, Subscription.Type type);

    List<Subscription> listSubscribedByEmail(String email);

    void subscribe(String email, Subscription.Type type);

    void unsubscribe(String email, Subscription.Type type);

}
