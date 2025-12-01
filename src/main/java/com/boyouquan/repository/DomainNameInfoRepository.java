package com.boyouquan.repository;

import com.boyouquan.model.DomainNameInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainNameInfoRepository extends CrudRepository<DomainNameInfo, Long> {

    @Query(value = """
            SELECT blog_domain_name as blogDomainName, real_domain_name as realDomainName,
                   registered_at as registeredAt, detected_at as detectedAt,
                   refreshed_at as refreshedAt, confirmed
            FROM domain_name_info
            WHERE blog_domain_name=:blogDomainName
            """, nativeQuery = true)
    DomainNameInfo getByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Modifying
    @Query(value = """
            INSERT INTO domain_name_info (blog_domain_name, real_domain_name, registered_at, detected_at, refreshed_at, confirmed)
            VALUES (:blogDomainName, :realDomainName, :registeredAt, NOW(), NOW(), false)
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName,
              @Param("realDomainName") String realDomainName,
              @Param("registeredAt") java.sql.Timestamp registeredAt);

    @Modifying
    @Query(value = """
            UPDATE domain_name_info SET registered_at=:registeredAt, refreshed_at=NOW()
            WHERE blog_domain_name=:blogDomainName
            """, nativeQuery = true)
    void update(@Param("blogDomainName") String blogDomainName,
                @Param("registeredAt") java.sql.Timestamp registeredAt);
}
