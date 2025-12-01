package com.boyouquan.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface LikeRepository extends Repository<Object, Long> {

    @Query(value = "SELECT IFNULL(SUM(amount),0) FROM likes WHERE type=:type AND entity_id=:entityId", nativeQuery = true)
    Long countByTypeAndEntityId(@Param("type") String type, @Param("entityId") Long entityId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes (year_month_str, type, entity_id, amount) VALUES (DATE_FORMAT(CURDATE(), '%Y/%m'), :type, :entityId, 1) ON DUPLICATE KEY UPDATE amount=amount+1", nativeQuery = true)
    void add(@Param("type") String type, @Param("entityId") Long entityId);
}
