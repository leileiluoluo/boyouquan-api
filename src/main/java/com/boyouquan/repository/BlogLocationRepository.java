package com.boyouquan.repository;

import com.boyouquan.model.BlogLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface BlogLocationRepository extends Repository<Object, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM blog_location WHERE domain_name=:domainName)", nativeQuery = true)
    boolean existsByDomainName(@Param("domainName") String domainName);

    @Query(value = "SELECT domain_name as domainName, location, created_at as createdAt, updated_at as updatedAt, deleted FROM blog_location WHERE deleted=false AND domain_name=:domainName", nativeQuery = true)
    BlogLocation getByDomainName(@Param("domainName") String domainName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO blog_location (domain_name, location, created_at, updated_at, deleted) VALUES (:domainName, :location, now(), now(), false)", nativeQuery = true)
    void save(@Param("domainName") String domainName, @Param("location") String location);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog_location SET location=:location, updated_at=now() WHERE domain_name=:domainName", nativeQuery = true)
    void update(@Param("domainName") String domainName, @Param("location") String location);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM blog_location WHERE domain_name=:domainName", nativeQuery = true)
    void deleteByDomainName(@Param("domainName") String domainName);
}
