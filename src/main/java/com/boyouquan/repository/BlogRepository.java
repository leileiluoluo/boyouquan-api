package com.boyouquan.repository;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogCollectedAt;
import com.boyouquan.model.PopularBlog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Repository
public interface BlogRepository extends Repository<Object, Long> {

    @Query(value = "WITH moment_activities AS ( SELECT m.blog_domain_name, MAX(m.created_at) as latest_active_time, 'moment' as type FROM moment m INNER JOIN blog b ON m.blog_domain_name=b.domain_name WHERE m.deleted=false AND b.draft=false AND b.deleted=false AND b.valid=true AND b.gravatar_valid=true GROUP BY m.blog_domain_name ), post_activities AS ( SELECT p.blog_domain_name, MAX(p.published_at) as latest_active_time, 'post' as type FROM post p INNER JOIN blog b ON p.blog_domain_name=b.domain_name WHERE p.deleted=false AND p.draft=false AND b.draft=false AND b.deleted=false AND b.valid=true AND b.gravatar_valid=true GROUP BY p.blog_domain_name ), combined_activities AS ( SELECT blog_domain_name, latest_active_time, type FROM moment_activities UNION ALL SELECT blog_domain_name, latest_active_time, type FROM post_activities ), ranked_activities AS ( SELECT blog_domain_name as blogDomainName, latest_active_time as latestActiveTime, type, ROW_NUMBER() OVER (PARTITION BY blog_domain_name ORDER BY latest_active_time DESC) as rn FROM combined_activities ) SELECT blogDomainName, latestActiveTime, type FROM ranked_activities WHERE rn=1 ORDER BY latestActiveTime DESC LIMIT :limit", nativeQuery = true)
    List<PopularBlog> listPopularBlogs(@Param("limit") int limit);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true AND gravatar_valid=:gravatarValid AND (:excludedSize=0 OR domain_name NOT IN (:excludedDomainNames)) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Blog> listByRandom(@Param("excludedDomainNames") List<String> excludedDomainNames, @Param("excludedSize") int excludedSize, @Param("gravatarValid") boolean gravatarValid, @Param("limit") int limit);

    @Query(value = "SELECT COUNT(1) FROM blog WHERE draft=false AND deleted=false AND valid=true", nativeQuery = true)
    Long countAll();

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true", nativeQuery = true)
    List<Blog> listAll();

    @Query(value = "SELECT DISTINCT domain_name FROM blog WHERE draft=false AND deleted=false AND valid=true", nativeQuery = true)
    List<String> listAllDomainNames();

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true ORDER BY collected_at DESC LIMIT :limit", nativeQuery = true)
    List<Blog> listRecentCollected(@Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM blog WHERE draft=false AND deleted=false AND valid=true AND (:keyword IS NULL OR :keyword='' OR (name LIKE CONCAT('%', :keyword, '%') OR address LIKE CONCAT('%', :keyword, '%')))", nativeQuery = true)
    Long countWithKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT b.domain_name as domainName, b.admin_email as adminEmail, b.name, b.address, b.rss_address as rssAddress, b.description, b.self_submitted as selfSubmitted, b.collected_at as collectedAt, b.updated_at as updatedAt, b.valid, b.gravatar_valid as gravatarValid, b.draft, b.deleted FROM blog b LEFT JOIN (SELECT blog_domain_name, SUM(amount) as accessCount FROM access GROUP BY blog_domain_name) a ON b.domain_name=a.blog_domain_name LEFT JOIN domain_name_info d ON b.domain_name=d.blog_domain_name WHERE b.draft=false AND b.deleted=false AND b.valid=true AND (:keyword IS NULL OR :keyword='' OR (b.name LIKE CONCAT('%', :keyword, '%') OR b.address LIKE CONCAT('%', :keyword, '%'))) ORDER BY CASE WHEN :sort IS NULL OR :sort='collect_time' THEN b.collected_at WHEN :sort='access_count' THEN a.accessCount ELSE d.registered_at END DESC LIMIT :offset, :rows", nativeQuery = true)
    List<Blog> listWithKeyWord(@Param("sort") String sort, @Param("keyword") String keyword, @Param("offset") int offset, @Param("rows") int rows);

    @Query(value = "SELECT domain_name AS domainName, collected_at AS collectedAt FROM blog GROUP BY domain_name ORDER BY collected_at", nativeQuery = true)
    List<BlogCollectedAt> listBlogCollectedAt();

    @Query(value = "SELECT EXISTS (SELECT 1 FROM blog WHERE domain_name=:domainName)", nativeQuery = true)
    boolean existsByDomainName(@Param("domainName") String domainName);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM blog WHERE rss_address=:rssAddress)", nativeQuery = true)
    boolean existsByRssAddress(@Param("rssAddress") String rssAddress);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true AND domain_name=:domainName", nativeQuery = true)
    Blog getByDomainName(@Param("domainName") String domainName);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true AND admin_email=:adminEmail", nativeQuery = true)
    List<Blog> listByAdminEmail(@Param("adminEmail") String adminEmail);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE draft=false AND deleted=false AND valid=true AND domain_name LIKE CONCAT(:shortDomainName, '%')", nativeQuery = true)
    Blog getByShortDomainName(@Param("shortDomainName") String shortDomainName);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE address=:address", nativeQuery = true)
    Blog getByAddress(@Param("address") String address);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE rss_address=:rssAddress", nativeQuery = true)
    Blog getByRSSAddress(@Param("rssAddress") String rssAddress);

    @Query(value = "SELECT domain_name as domainName, admin_email as adminEmail, name, address, rss_address as rssAddress, description, self_submitted as selfSubmitted, collected_at as collectedAt, updated_at as updatedAt, valid, gravatar_valid as gravatarValid, draft, deleted FROM blog WHERE md5(admin_email)=:md5AdminEmail", nativeQuery = true)
    Blog getByMd5AdminEmail(@Param("md5AdminEmail") String md5AdminEmail);

    @Query(value = "SELECT TIMESTAMPDIFF(YEAR, collected_at, CURDATE()) FROM blog WHERE draft=false AND deleted=false AND valid=true AND domain_name=:domainName", nativeQuery = true)
    Integer getJoinYearsByDomainName(@Param("domainName") String domainName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO blog (domain_name, admin_email, name, address, rss_address, description, self_submitted, collected_at, updated_at, valid, draft, deleted) VALUES (:domainName, :adminEmail, :name, :address, :rssAddress, :description, :selfSubmitted, :collectedAt, :updatedAt, true, :draft, false)", nativeQuery = true)
    void save(@Param("domainName") String domainName, @Param("adminEmail") String adminEmail, @Param("name") String name, @Param("address") String address, @Param("rssAddress") String rssAddress, @Param("description") String description, @Param("selfSubmitted") boolean selfSubmitted, @Param("collectedAt") java.util.Date collectedAt, @Param("updatedAt") java.util.Date updatedAt, @Param("draft") boolean draft);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog SET admin_email=:adminEmail, name=:name, address=:address, rss_address=:rssAddress, description=:description, self_submitted=:selfSubmitted, collected_at=:collectedAt, updated_at=now(), valid=:valid, draft=:draft WHERE domain_name=:domainName", nativeQuery = true)
    void update(@Param("domainName") String domainName, @Param("adminEmail") String adminEmail, @Param("name") String name, @Param("address") String address, @Param("rssAddress") String rssAddress, @Param("description") String description, @Param("selfSubmitted") boolean selfSubmitted, @Param("collectedAt") java.util.Date collectedAt, @Param("valid") boolean valid, @Param("draft") boolean draft);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog SET gravatar_valid=:gravatarValid WHERE domain_name=:domainName", nativeQuery = true)
    void updateGravatarValidFlag(@Param("domainName") String domainName, @Param("gravatarValid") boolean gravatarValid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog SET valid=false, updated_at=now() WHERE domain_name=:domainName", nativeQuery = true)
    void markAsInvalid(@Param("domainName") String domainName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog SET deleted=true, updated_at=now() WHERE domain_name=:domainName", nativeQuery = true)
    void deleteByDomainName(@Param("domainName") String domainName);
}
