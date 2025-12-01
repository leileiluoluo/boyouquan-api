package com.boyouquan.repository;

import com.boyouquan.model.DomainNameChange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface DomainNameChangeRepository extends Repository<Object, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM domain_name_change WHERE deleted=false AND old_domain_name=:oldDomainName)", nativeQuery = true)
    boolean existsByOldDomainName(@Param("oldDomainName") String oldDomainName);

    @Query(value = "SELECT id, old_domain_name as oldDomainName, new_domain_name as newDomainName, changed_at as changedAt, deleted FROM domain_name_change WHERE deleted=false AND old_domain_name=:oldDomainName", nativeQuery = true)
    DomainNameChange getByOldDomainName(@Param("oldDomainName") String oldDomainName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO domain_name_change (old_domain_name, new_domain_name, changed_at, deleted) VALUES (:oldDomainName, :newDomainName, now(), false)", nativeQuery = true)
    void save(@Param("oldDomainName") String oldDomainName, @Param("newDomainName") String newDomainName);
}
