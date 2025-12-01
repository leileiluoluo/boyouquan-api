package com.boyouquan.repository;

import com.boyouquan.model.BlogStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogStatusRepository extends CrudRepository<BlogStatus, Long> {

    @Query(value = """
            SELECT blog_domain_name as blogDomainName, status, code, reason, detected_at as detectedAt, deleted
            FROM blog_status
            WHERE blog_domain_name=:blogDomainName AND deleted=false
            ORDER BY detected_at DESC LIMIT 1
            """, nativeQuery = true)
    BlogStatus getLatestByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Modifying
    @Query(value = """
            INSERT INTO blog_status (blog_domain_name, status, code, reason, detected_at, deleted)
            VALUES (:blogDomainName, :status, :code, :reason, NOW(), false)
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName,
              @Param("status") String status,
              @Param("code") Integer code,
              @Param("reason") String reason);

    @Modifying
    @Query(value = """
            DELETE FROM blog_status WHERE blog_domain_name=:blogDomainName
            """, nativeQuery = true)
    void deleteByBlogDomainName(@Param("blogDomainName") String blogDomainName);
}
