package com.boyouquan.repository;

import com.boyouquan.model.EmailLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface EmailLogRepository extends Repository<Object, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM email_log WHERE blog_domain_name=:blogDomainName AND type=:type)", nativeQuery = true)
    boolean existsByBlogDomainNameAndType(@Param("blogDomainName") String blogDomainName, @Param("type") String type);

    @Query(value = "SELECT blog_domain_name as blogDomainName, email, type, sent_at as sentAt FROM email_log WHERE blog_domain_name=:blogDomainName AND type=:type ORDER BY sent_at DESC LIMIT 1", nativeQuery = true)
    EmailLog getLatestByBlogDomainNameAndType(@Param("blogDomainName") String blogDomainName, @Param("type") String type);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO email_log (blog_domain_name, email, type, sent_at) VALUES (:blogDomainName, :email, :type, now())", nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName, @Param("email") String email, @Param("type") String type);
}
