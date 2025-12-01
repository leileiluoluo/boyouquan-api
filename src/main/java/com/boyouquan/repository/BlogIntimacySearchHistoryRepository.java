package com.boyouquan.repository;

import com.boyouquan.model.BlogIntimacySearchHistory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogIntimacySearchHistoryRepository extends CrudRepository<BlogIntimacySearchHistory, Long> {

    @Query(value = """
            SELECT id, source_blog_domain_name as sourceBlogDomainName, target_blog_domain_name as targetBlogDomainName,
                   path_length as pathLength, ip_address as ipAddress, searched_at as searchedAt
            FROM (
              SELECT source_blog_domain_name, target_blog_domain_name, MAX(id) as id,
                     MAX(path_length) as path_length, MAX(ip_address) as ip_address, MAX(searched_at) as searched_at
              FROM blog_intimacy_search_history
              GROUP BY source_blog_domain_name, target_blog_domain_name
              ORDER BY source_blog_domain_name, target_blog_domain_name, MAX(searched_at) DESC
            ) t ORDER BY t.searched_at DESC LIMIT :size
            """, nativeQuery = true)
    List<BlogIntimacySearchHistory> listLatest(@Param("size") int size);

    @Modifying
    @Query(value = """
            INSERT INTO blog_intimacy_search_history (source_blog_domain_name, target_blog_domain_name, path_length, ip_address, searched_at)
            VALUES (:sourceBlogDomainName, :targetBlogDomainName, :pathLength, :ipAddress, NOW())
            """, nativeQuery = true)
    void save(@Param("sourceBlogDomainName") String sourceBlogDomainName,
              @Param("targetBlogDomainName") String targetBlogDomainName,
              @Param("pathLength") Integer pathLength,
              @Param("ipAddress") String ipAddress);
}
