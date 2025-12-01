package com.boyouquan.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PinHistoryRepository extends CrudRepository<com.boyouquan.model.PinHistory, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO pin_history (blog_domain_name, link, pinned_at)
            VALUES (:blogDomainName, :link, NOW())
            ON DUPLICATE KEY UPDATE pinned_at=NOW()
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName, @Param("link") String link);

    @Query(value = """
            SELECT link FROM pin_history
            WHERE blog_domain_name=:blogDomainName AND pinned_at >= :startDate
            ORDER BY pinned_at DESC
            """, nativeQuery = true)
    List<String> listLinksByBlogDomainName(@Param("blogDomainName") String blogDomainName,
                                           @Param("startDate") Date startDate);
}
