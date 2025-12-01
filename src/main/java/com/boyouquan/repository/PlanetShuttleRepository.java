package com.boyouquan.repository;

import com.boyouquan.model.BlogDomainNameInitiated;
import com.boyouquan.model.MonthInitiated;
import com.boyouquan.model.PlanetShuttle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanetShuttleRepository extends CrudRepository<PlanetShuttle, Long> {

    @Query(value = """
            SELECT blog_domain_name AS blogDomainName, SUM(amount) AS initiatedCount
            FROM planet_shuttle
            WHERE year_month_str=DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y/%m')
            GROUP BY blog_domain_name
            ORDER BY SUM(amount) DESC
            LIMIT 1
            """, nativeQuery = true)
    BlogDomainNameInitiated getMostInitiatedBlogDomainNameInLastMonth();

    @Query(value = """
            SELECT MAX(year_month_str) FROM planet_shuttle WHERE blog_domain_name=:blogDomainName
            """, nativeQuery = true)
    String getLatestInitiatedYearMonthStr(@Param("blogDomainName") String blogDomainName);

    @Query(value = """
            SELECT v.month AS month, IFNULL(SUM(ps.amount), 0) AS count
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
            LEFT JOIN planet_shuttle ps ON v.month=ps.year_month_str AND ps.blog_domain_name=:blogDomainName
            GROUP BY v.month
            """, nativeQuery = true)
    List<MonthInitiated> getBlogInitiatedSeriesInLatestOneYear(@Param("blogDomainName") String blogDomainName);

    @Query(value = """
            SELECT IFNULL(SUM(amount), 0) FROM planet_shuttle WHERE blog_domain_name=:blogDomainName
            """, nativeQuery = true)
    Long countInitiatedByBlogDomainName(@Param("blogDomainName") String blogDomainName);

    @Modifying
    @Query(value = """
            INSERT INTO planet_shuttle (year_month_str, blog_domain_name, to_blog_address, amount)
            VALUES (DATE_FORMAT(CURDATE(), '%Y/%m'), :blogDomainName, :toBlogAddress, 1)
            ON DUPLICATE KEY UPDATE amount=amount+1
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName, @Param("toBlogAddress") String toBlogAddress);
}
