package com.boyouquan.repository;

import com.boyouquan.model.EmailValidation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface EmailValidationRepository extends Repository<Object, Long> {

    @Query(value = "SELECT id, email, code, issued_at as issuedAt, deleted FROM email_validation WHERE email=:email AND code=:code ORDER BY issued_at DESC LIMIT 1", nativeQuery = true)
    EmailValidation getByEmailAndCode(@Param("email") String email, @Param("code") String code);

    @Query(value = "SELECT COUNT(*) FROM email_validation WHERE email=:email AND DATE(issued_at)=CURDATE()", nativeQuery = true)
    int countTodayIssuedByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO email_validation (email, code, issued_at, deleted) VALUES (:email, :code, now(), false)", nativeQuery = true)
    void save(@Param("email") String email, @Param("code") String code);
}
