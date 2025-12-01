package com.boyouquan.repository;

import com.boyouquan.model.BlogStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface BlogStatusRepository extends Repository<Object, Long> {

    @Query(value = "SELECT blog_domain_name as blogDomainName, status, code, reason, detected_at as detectedAt, deleted FROM blog_status WHERE blog_domain_name=:blogDomainName AND deleted=false ORDER BY detected_at DESC LIMIT 1", nativeQuery = true)
    BlogStatus getLatestByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO blog_status (blog_domain_name, status, code, reason, detected_at, deleted) VALUES (:blogDomainName, :status, :code, :reason, now(), false)", nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName, @Param("status") String status, @Param("code") String code, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM blog_status WHERE blog_domain_name=:blogDomainName", nativeQuery = true)
    void deleteByBlogDomainName(@Param("blogDomainName") String blogDomainName);
}
