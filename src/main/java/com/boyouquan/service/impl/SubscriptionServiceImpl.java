package com.boyouquan.service.impl;

import com.boyouquan.dao.SubscriptionDaoMapper;
import com.boyouquan.model.Subscription;
import com.boyouquan.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDaoMapper subscriptionDaoMapper;

    @Override
    public List<Subscription> listSubscribedByType(Subscription.Type type) {
        return subscriptionDaoMapper.listSubscribedByType(type);
    }

    @Override
    public List<Subscription> listSubscribedByEmail(String email) {
        return subscriptionDaoMapper.listSubscribedByEmail(email);
    }

    @Override
    public boolean existsSubscribedByEmailAndType(String email, Subscription.Type type) {
        return subscriptionDaoMapper.existsSubscribedByEmailAndType(email, type);
    }

    @Override
    public boolean existsSubscribedByEmail(String email) {
        return subscriptionDaoMapper.existsSubscribedByEmail(email);
    }

    @Override
    public void subscribe(String email, Subscription.Type type) {
        subscriptionDaoMapper.subscribe(email, type);
    }

    @Override
    public void unsubscribe(String email, Subscription.Type type) {
        subscriptionDaoMapper.unsubscribe(email, type);
    }

}
