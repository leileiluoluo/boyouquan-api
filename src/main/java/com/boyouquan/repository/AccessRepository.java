package com.boyouquan.repository;

import com.boyouquan.entity.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface AccessRepository extends CrudRepository<Access, AccessPrimaryKey> {

    @Query(value = "SELECT IFNULL(SUM(amount), 0) FROM access", nativeQuery = true)
    Long countAll();

    @Query(value = """
            SELECT blog_domain_name AS blogDomainName, SUM(amount) AS accessCount
            FROM access
            WHERE year_month_str=DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y/%m')
            GROUP BY blog_domain_name
            ORDER BY SUM(amount) DESC
            LIMIT 1
            """, nativeQuery = true)
    BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth();

    @Query(value = """
            SELECT link, SUM(amount) AS accessCount
            FROM access
            WHERE blog_domain_name=:blogDomainName
              AND link != :blogAddress
              AND year_month_str >= DATE_FORMAT(:startDate, '%Y/%m')
            GROUP BY link
            ORDER BY SUM(amount) DESC
            LIMIT 1
            """, nativeQuery = true)
    BlogLinkAccess getMostAccessedLinkByBlogDomainName(String blogDomainName, String blogAddress, Date startDate);

    @Query(value = """
            SELECT v.month AS month, IFNULL(SUM(a.amount), 0) AS count
            FROM (
              SELECT DATE_FORMAT((CURDATE() - INTERVAL 11 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 10 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 9 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 8 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 7 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 6 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y/%m') AS `month`
              UNION SELECT DATE_FORMAT(CURDATE(), '%Y/%m') AS `month`
            ) v
            LEFT JOIN access a ON v.month=a.year_month_str AND a.blog_domain_name=:blogDomainName
            GROUP BY v.month
            """, nativeQuery = true)
    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    @Query(value = "SELECT IFNULL(SUM(amount), 0) FROM access WHERE blog_domain_name=:blogDomainName", nativeQuery = true)
    Long countByBlogDomainName(String blogDomainName);

    @Query(value = """
            SELECT IFNULL(SUM(amount), 0) FROM access
            WHERE blog_domain_name=:blogDomainName AND year_month_str >= DATE_FORMAT(:startDate, '%Y/%m')
            """, nativeQuery = true)
    Long countByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    @Query(value = """
            SELECT blog_domain_name AS blogDomainName, IFNULL(SUM(amount), 0) AS count
            FROM access
            WHERE year_month_str >= DATE_FORMAT(:startDate, '%Y/%m')
            GROUP BY blog_domain_name
            ORDER BY IFNULL(SUM(amount), 0) DESC
            """, nativeQuery = true)
    List<BlogAccessCount> listBlogAccessCount(Date startDate);

    @Query(value = "SELECT IFNULL(SUM(amount), 0) FROM access WHERE link=:link", nativeQuery = true)
    Long countByLink(String link);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO access (year_month_str, blog_domain_name, link, amount)
            VALUES (DATE_FORMAT(CURDATE(), '%Y/%m'), :blogDomainName, :link, 1)
            ON DUPLICATE KEY UPDATE amount=amount+1
            """, nativeQuery = true)
    void save(String blogDomainName, String link);

    @Transactional
    void deleteByBlogDomainName(String blogDomainName);

}
