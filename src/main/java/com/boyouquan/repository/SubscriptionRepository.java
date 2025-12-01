package com.boyouquan.repository;

import com.boyouquan.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Repository
public interface SubscriptionRepository extends Repository<Object, Long> {

    @Query(value = "SELECT email, type, subscribed_at as subscribedAt, unsubscribed_at as unsubscribedAt, unsubscribed FROM subscription WHERE unsubscribed=false AND type=:type ORDER BY subscribed_at", nativeQuery = true)
    List<Subscription> listSubscribedByType(@Param("type") String type);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM subscription WHERE unsubscribed=false AND email=:email)", nativeQuery = true)
    boolean existsSubscribedByEmail(@Param("email") String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM subscription WHERE unsubscribed=false AND email=:email AND type=:type)", nativeQuery = true)
    boolean existsSubscribedByEmailAndType(@Param("email") String email, @Param("type") String type);

    @Query(value = "SELECT email, type, subscribed_at as subscribedAt, unsubscribed_at as unsubscribedAt, unsubscribed FROM subscription WHERE unsubscribed=false AND email=:email ORDER BY subscribed_at DESC", nativeQuery = true)
    List<Subscription> listSubscribedByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO subscription (email, type, subscribed_at, unsubscribed) VALUES (:email, :type, now(), false)", nativeQuery = true)
    void subscribe(@Param("email") String email, @Param("type") String type);

    @Modifying
    @Transactional
    @Query(value = "UPDATE subscription SET unsubscribed=true, unsubscribed_at=now() WHERE unsubscribed=false AND email=:email AND type=:type", nativeQuery = true)
    void unsubscribe(@Param("email") String email, @Param("type") String type);
}
