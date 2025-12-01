package com.boyouquan.repository;

import com.boyouquan.model.SelectedPostAccess;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.stereotype.Repository
public interface MonthlySelectedRepository extends Repository<Object, Long> {

    // includeCurrentMonth handling approximated using conditional OR logic
    @Query(value = "SELECT DISTINCT a.year_month_str FROM access a JOIN blog b ON a.blog_domain_name=b.domain_name JOIN post p ON a.blog_domain_name=p.blog_domain_name AND a.link=p.link WHERE (:includeCurrentMonth=true OR a.year_month_str!=DATE_FORMAT(CURDATE(), '%Y/%m')) AND a.link!=b.address AND p.recommended=true GROUP BY a.year_month_str ORDER BY a.year_month_str DESC", nativeQuery = true)
    List<String> listYearMonthStrs(@Param("includeCurrentMonth") boolean includeCurrentMonth);

    @Query(value = "SELECT popular.yearMonthStr, popular.postLink, popular.publishedAt, popular.blogDomainName, popular.accessCount, (pinned.postLink IS NOT NULL) FROM ( SELECT a.year_month_str as yearMonthStr, a.link as postLink, p.published_at as publishedAt, max(b.domain_name) as blogDomainName, sum(a.amount) as accessCount FROM access a, blog b, post p WHERE a.blog_domain_name=b.domain_name AND a.blog_domain_name=p.blog_domain_name AND a.link=p.link AND a.link!=b.address AND p.recommended=true AND a.year_month_str=:yearMonthStr AND p.published_at between concat(:yearMonthStr, '/01') and concat(:yearMonthStr, '/31') GROUP BY a.link ) popular LEFT JOIN ( SELECT link as postLink, blog_domain_name as blogDomainName FROM pin_history WHERE pinned_at between concat(:yearMonthStr, '/01') and concat(:yearMonthStr, '/31') ) pinned ON popular.postLink=pinned.postLink ORDER BY (pinned.postLink IS NOT NULL) DESC, popular.accessCount DESC LIMIT :limit", nativeQuery = true)
    List<SelectedPostAccess> listSelectedPostsByYearMonthStr(@Param("yearMonthStr") String yearMonthStr, @Param("limit") int limit);
}
