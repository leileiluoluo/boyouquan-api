package com.boyouquan.repository;

import com.boyouquan.model.EmailLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends CrudRepository<EmailLog, Long> {

    @Query(value = """
            SELECT EXISTS (
              SELECT 1 FROM email_log WHERE blog_domain_name=:blogDomainName AND type=:type
            )
            """, nativeQuery = true)
    boolean existsByBlogDomainNameAndType(@Param("blogDomainName") String blogDomainName,
                                          @Param("type") EmailLog.Type type);

    @Query(value = """
            SELECT blog_domain_name as blogDomainName, email, type, sent_at as sentAt
            FROM email_log
            WHERE blog_domain_name=:blogDomainName AND type=:type
            ORDER BY sent_at DESC
            LIMIT 1
            """, nativeQuery = true)
    EmailLog getLatestByBlogDomainNameAndType(@Param("blogDomainName") String blogDomainName,
                                              @Param("type") EmailLog.Type type);

    @Modifying
    @Query(value = """
            INSERT INTO email_log (blog_domain_name, email, type, sent_at)
            VALUES (:blogDomainName, :email, :type, NOW())
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName,
              @Param("email") String email,
              @Param("type") EmailLog.Type type);
}
