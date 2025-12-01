package com.boyouquan.repository;

import com.boyouquan.model.Like;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {

    @Query(value = """
            SELECT IFNULL(SUM(amount), 0) FROM likes WHERE type=:type AND entity_id=:entityId
            """, nativeQuery = true)
    Long countByTypeAndEntityId(@Param("type") Like.Type type, @Param("entityId") Long entityId);

    @Modifying
    @Query(value = """
            INSERT INTO likes (year_month_str, type, entity_id, amount)
            VALUES (DATE_FORMAT(CURDATE(), '%Y/%m'), :type, :entityId, 1)
            ON DUPLICATE KEY UPDATE amount=amount+1
            """, nativeQuery = true)
    void add(@Param("type") Like.Type type, @Param("entityId") Long entityId);
}
