package com.boyouquan.repository;

import com.boyouquan.model.BlogRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Repository
public interface BlogRequestRepository extends Repository<Object, Long> {

    @Query(value = "SELECT id, rss_address as rssAddress, admin_email as adminEmail, name, description, self_submitted as selfSubmitted, requested_at as requestedAt, updated_at as updatedAt, status, reason, deleted FROM blog_request WHERE self_submitted=:selfSubmitted AND status IN (:statuses) AND (:keyword IS NULL OR :keyword='' OR (name LIKE CONCAT('%', :keyword, '%') OR rss_address LIKE CONCAT('%', :keyword, '%') OR admin_email LIKE CONCAT('%', :keyword, '%'))) ORDER BY requested_at DESC LIMIT :offset, :rows", nativeQuery = true)
    List<BlogRequest> listBySelfSubmittedAndStatuses(@Param("keyword") String keyword, @Param("selfSubmitted") boolean selfSubmitted, @Param("statuses") List<String> statuses, @Param("offset") int offset, @Param("rows") int rows);

    @Query(value = "SELECT COUNT(*) FROM blog_request WHERE self_submitted=:selfSubmitted AND status IN (:statuses) AND (:keyword IS NULL OR :keyword='' OR (name LIKE CONCAT('%', :keyword, '%') OR rss_address LIKE CONCAT('%', :keyword, '%') OR admin_email LIKE CONCAT('%', :keyword, '%')))", nativeQuery = true)
    Long countBySelfSubmittedAndStatuses(@Param("keyword") String keyword, @Param("selfSubmitted") boolean selfSubmitted, @Param("statuses") List<String> statuses);

    @Query(value = "SELECT id, rss_address as rssAddress, admin_email as adminEmail, name, description, self_submitted as selfSubmitted, requested_at as requestedAt, updated_at as updatedAt, status, reason, deleted FROM blog_request WHERE status IN (:statuses) AND (:keyword IS NULL OR :keyword='' OR (name LIKE CONCAT('%', :keyword, '%') OR rss_address LIKE CONCAT('%', :keyword, '%'))) ORDER BY requested_at DESC LIMIT :offset, :rows", nativeQuery = true)
    List<BlogRequest> listByStatuses(@Param("keyword") String keyword, @Param("statuses") List<String> statuses, @Param("offset") int offset, @Param("rows") int rows);

    @Query(value = "SELECT COUNT(*) FROM blog_request WHERE status IN (:statuses) AND (:keyword IS NULL OR :keyword='' OR (name LIKE CONCAT('%', :keyword, '%') OR rss_address LIKE CONCAT('%', :keyword, '%')))", nativeQuery = true)
    Long countByStatuses(@Param("keyword") String keyword, @Param("statuses") List<String> statuses);

    @Query(value = "SELECT id, rss_address as rssAddress, admin_email as adminEmail, name, description, self_submitted as selfSubmitted, requested_at as requestedAt, updated_at as updatedAt, status, reason, deleted FROM blog_request WHERE status=:status ORDER BY requested_at DESC", nativeQuery = true)
    List<BlogRequest> listByStatus(@Param("status") String status);

    @Query(value = "SELECT id, rss_address as rssAddress, admin_email as adminEmail, name, description, self_submitted as selfSubmitted, requested_at as requestedAt, updated_at as updatedAt, status, reason, deleted FROM blog_request WHERE id=:id", nativeQuery = true)
    BlogRequest getById(@Param("id") Long id);

    @Query(value = "SELECT id, rss_address as rssAddress, admin_email as adminEmail, name, description, self_submitted as selfSubmitted, requested_at as requestedAt, updated_at as updatedAt, status, reason, deleted FROM blog_request WHERE rss_address=:rssAddress", nativeQuery = true)
    BlogRequest getByRssAddress(@Param("rssAddress") String rssAddress);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog_request SET admin_email=:adminEmail, name=:name, description=:description, updated_at=now(), status=:status, reason=:reason WHERE rss_address=:rssAddress", nativeQuery = true)
    void update(@Param("rssAddress") String rssAddress, @Param("adminEmail") String adminEmail, @Param("name") String name, @Param("description") String description, @Param("status") String status, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO blog_request (rss_address, admin_email, name, description, self_submitted, requested_at, updated_at, status, reason, deleted) VALUES (:rssAddress, :adminEmail, :name, :description, :selfSubmitted, now(), now(), 'submitted', :reason, false)", nativeQuery = true)
    void submit(@Param("rssAddress") String rssAddress, @Param("adminEmail") String adminEmail, @Param("name") String name, @Param("description") String description, @Param("selfSubmitted") boolean selfSubmitted, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM blog_request WHERE rss_address=:rssAddress", nativeQuery = true)
    void deleteByRssAddress(@Param("rssAddress") String rssAddress);
}
