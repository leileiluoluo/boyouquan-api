package com.boyouquan.repository;

import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.BlogPostCount;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public interface PostRepository extends Repository<Object, Long> {

    @Query(value = "SELECT blog_domain_name as blogDomainName, COUNT(*) as postCount FROM post WHERE draft=false AND deleted=false AND date(published_at) >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) GROUP BY blog_domain_name ORDER BY postCount DESC LIMIT 1", nativeQuery = true)
    BlogDomainNamePublish getMostPublishedInLatestOneMonth();

    @Query(value = "SELECT v.month, IFNULL(b.count,0) count FROM ( SELECT DATE_FORMAT((CURDATE() - INTERVAL 11 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 10 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 9 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 8 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 7 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 6 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y/%m') AS month UNION SELECT DATE_FORMAT(CURDATE(), '%Y/%m') AS month ) v LEFT JOIN ( SELECT DATE_FORMAT(published_at,'%Y/%m') as month, COUNT(*) as count FROM post WHERE DATE_FORMAT(published_at,'%Y/%m') > DATE_FORMAT(date_sub(curdate(), interval 12 month),'%Y/%m') AND blog_domain_name=:blogDomainName GROUP BY month ) b ON v.month=b.month GROUP BY v.month", nativeQuery = true)
    List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(@Param("blogDomainName") String blogDomainName);

    @Query(value = "SELECT COUNT(*) FROM post WHERE draft=false AND deleted=false AND description IS NOT NULL AND ((:sort IS NULL OR :sort='recommended') AND recommended=true OR (:sort IS NOT NULL AND :sort<>'recommended')) AND (:keyword IS NULL OR :keyword='' OR title LIKE CONCAT('%', :keyword, '%'))", nativeQuery = true)
    Long countWithKeyWord(@Param("sort") String sort, @Param("keyword") String keyword);

    @Query(value = "SELECT link, blog_domain_name as blogDomainName, title, description, published_at as publishedAt, draft, recommended, pinned, deleted FROM post WHERE draft=false AND deleted=false AND description IS NOT NULL AND ((:sort IS NULL OR :sort='recommended') AND recommended=true OR (:sort IS NOT NULL AND :sort<>'recommended')) AND (:keyword IS NULL OR :keyword='' OR title LIKE CONCAT('%', :keyword, '%')) ORDER BY CASE WHEN :sort IS NULL OR :sort='recommended' THEN pinned END DESC, published_at DESC LIMIT :offset, :rows", nativeQuery = true)
    List<Post> listWithKeyWord(@Param("sort") String sort, @Param("keyword") String keyword, @Param("offset") int offset, @Param("rows") int rows);

    @Query(value = "SELECT COUNT(*) FROM post WHERE draft=false AND deleted=false", nativeQuery = true)
    Long countAll();

    @Query(value = "SELECT MAX(published_at) FROM post WHERE draft=false AND deleted=false AND blog_domain_name=:blogDomainName", nativeQuery = true)
    Date getLatestPublishedAtByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Query(value = "SELECT COUNT(*) FROM post WHERE draft=false AND deleted=false AND blog_domain_name=:blogDomainName", nativeQuery = true)
    Long countByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Query(value = "SELECT COUNT(*) FROM post WHERE draft=false AND deleted=false AND blog_domain_name=:blogDomainName AND published_at >= :startDate", nativeQuery = true)
    Long countByBlogDomainNameAndStartDate(@Param("blogDomainName") String blogDomainName, @Param("startDate") Date startDate);

    @Query(value = "SELECT COUNT(*) FROM post WHERE deleted=false AND draft=:draft AND blog_domain_name=:blogDomainName", nativeQuery = true)
    Long countByDraftAndBlogDomainName(@Param("draft") boolean draft, @Param("blogDomainName") String blogDomainName);

    @Query(value = "SELECT link, blog_domain_name as blogDomainName, title, description, published_at as publishedAt, draft, recommended, pinned, deleted FROM post WHERE deleted=false AND draft=:draft AND blog_domain_name=:blogDomainName ORDER BY published_at DESC LIMIT :offset, :rows", nativeQuery = true)
    List<Post> listByDraftAndBlogDomainName(@Param("draft") boolean draft, @Param("blogDomainName") String blogDomainName, @Param("offset") int offset, @Param("rows") int rows);

    @Query(value = "SELECT link, blog_domain_name as blogDomainName, title, description, published_at as publishedAt, draft, recommended, pinned, deleted FROM post WHERE deleted=false AND draft=false AND recommended=true AND blog_domain_name=:blogDomainName AND published_at >= :startDate ORDER BY published_at DESC", nativeQuery = true)
    List<Post> listRecommendedByBlogDomainName(@Param("blogDomainName") String blogDomainName, @Param("startDate") Date startDate);

    @Query(value = "SELECT blog_domain_name as blogDomainName, COUNT(*) as count FROM post WHERE deleted=false AND draft=false AND published_at >= :startDate GROUP BY blog_domain_name ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<BlogPostCount> listBlogPostCount(@Param("startDate") Date startDate);

    @Query(value = "SELECT link, blog_domain_name as blogDomainName, title, description, published_at as publishedAt, draft, recommended, pinned, deleted FROM post WHERE link=:link", nativeQuery = true)
    Post getByLink(@Param("link") String link);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM post WHERE link=:link)", nativeQuery = true)
    boolean existsByLink(@Param("link") String link);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM post WHERE blog_domain_name=:blogDomainName AND title=:title)", nativeQuery = true)
    boolean existsByTitle(@Param("blogDomainName") String blogDomainName, @Param("title") String title);

    // batchSave simplified: use single-row insert in loop externally (native multi-values binding not handled here)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO post (link, blog_domain_name, title, description, published_at, draft, deleted) VALUES (:link, :blogDomainName, :title, :description, :publishedAt, :draft, false)", nativeQuery = true)
    void save(@Param("link") String link, @Param("blogDomainName") String blogDomainName, @Param("title") String title, @Param("description") String description, @Param("publishedAt") Date publishedAt, @Param("draft") boolean draft);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET draft=:draft WHERE blog_domain_name=:blogDomainName", nativeQuery = true)
    void batchUpdateDraftByBlogDomainName(@Param("blogDomainName") String blogDomainName, @Param("draft") boolean draft);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE blog_domain_name=:blogDomainName", nativeQuery = true)
    void deleteByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE link=:link", nativeQuery = true)
    void deleteByLink(@Param("link") String link);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET recommended=true WHERE link=:link", nativeQuery = true)
    void recommendByLink(@Param("link") String link);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET pinned=false WHERE link=:link", nativeQuery = true)
    void unpinByLink(@Param("link") String link);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET pinned=true WHERE link=:link", nativeQuery = true)
    void pinByLink(@Param("link") String link);
}
