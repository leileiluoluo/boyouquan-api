package com.boyouquan.repository;

import com.boyouquan.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<Object, Long> {

    @Query(value = "SELECT username, md5password as md5Password, role, deleted FROM user WHERE deleted=false AND username=:username", nativeQuery = true)
    User getUserByUsername(@Param("username") String username);
}
