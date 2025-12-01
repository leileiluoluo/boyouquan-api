package com.boyouquan.repository;

import com.boyouquan.model.FriendLink;
import com.boyouquan.model.BlogShortInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public interface FriendLinkRepository extends Repository<Object, Long> {

    @Query(value = "SELECT id, source_blog_domain_name as sourceBlogDomainName, target_blog_domain_name as targetBlogDomainName, page_title as pageTitle, page_url as pageUrl, created_by_job as createdByJob, created_at as createdAt FROM friend_link", nativeQuery = true)
    List<FriendLink> listAll();

    @Query(value = "SELECT id, source_blog_domain_name as sourceBlogDomainName, target_blog_domain_name as targetBlogDomainName, page_title as pageTitle, page_url as pageUrl, created_by_job as createdByJob, created_at as createdAt FROM friend_link WHERE source_blog_domain_name=:domainName", nativeQuery = true)
    List<FriendLink> listBySourceBlogDomainName(@Param("domainName") String domainName);

    @Query(value = "SELECT id, source_blog_domain_name as sourceBlogDomainName, target_blog_domain_name as targetBlogDomainName, page_title as pageTitle, page_url as pageUrl, created_by_job as createdByJob, created_at as createdAt FROM friend_link WHERE source_blog_domain_name=:sourceDomainName AND target_blog_domain_name=:targetDomainName", nativeQuery = true)
    FriendLink getBySourceAndTargetBlogDomainName(@Param("sourceDomainName") String sourceDomainName, @Param("targetDomainName") String targetDomainName);

    @Query(value = "SELECT DISTINCT target_blog_domain_name FROM friend_link WHERE source_blog_domain_name=:domainName", nativeQuery = true)
    List<String> getDirectTargetBlogDomainNames(@Param("domainName") String domainName);

    @Query(value = "SELECT DISTINCT source_blog_domain_name FROM friend_link WHERE target_blog_domain_name=:domainName", nativeQuery = true)
    List<String> getDirectSourceBlogDomainNames(@Param("domainName") String domainName);

    @Query(value = "SELECT name as blogName, domain_name as domainName FROM blog b WHERE b.draft=false AND b.deleted=false AND EXISTS (SELECT 1 FROM friend_link fl WHERE fl.source_blog_domain_name=b.domain_name)", nativeQuery = true)
    List<BlogShortInfo> listAllSourceBlogs();

    @Query(value = "SELECT name as blogName, domain_name as domainName FROM blog b WHERE b.draft=false AND b.deleted=false AND EXISTS (SELECT 1 FROM friend_link fl WHERE fl.target_blog_domain_name=b.domain_name)", nativeQuery = true)
    List<BlogShortInfo> listAllTargetBlogs();

    @Query(value = "SELECT max(created_at) FROM friend_link WHERE created_by_job=true", nativeQuery = true)
    Date getJobMaxCreatedAt();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM friend_link WHERE source_blog_domain_name=:domainName", nativeQuery = true)
    void deleteBySourceBlogDomainName(@Param("domainName") String domainName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM friend_link WHERE target_blog_domain_name=:domainName", nativeQuery = true)
    void deleteByTargetBlogDomainName(@Param("domainName") String domainName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO friend_link (source_blog_domain_name, target_blog_domain_name, page_title, page_url, created_by_job, created_at) VALUES (:sourceBlogDomainName, :targetBlogDomainName, :pageTitle, :pageUrl, :createdByJob, now())", nativeQuery = true)
    void save(@Param("sourceBlogDomainName") String sourceBlogDomainName, @Param("targetBlogDomainName") String targetBlogDomainName, @Param("pageTitle") String pageTitle, @Param("pageUrl") String pageUrl, @Param("createdByJob") boolean createdByJob);
}
