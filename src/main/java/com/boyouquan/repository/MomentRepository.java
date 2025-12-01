package com.boyouquan.repository;

import com.boyouquan.model.Moment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentRepository extends CrudRepository<Moment, Long> {

    @Query(value = """
            SELECT id, blog_domain_name as blogDomainName, description, image_url as imageURL,
                   created_at as createdAt, updated_at as updatedAt, deleted
            FROM moment
            WHERE deleted=false
            ORDER BY created_at DESC
            LIMIT :offset, :rows
            """, nativeQuery = true)
    List<Moment> list(@Param("offset") int offset, @Param("rows") int rows);

    @Query(value = """
            SELECT COUNT(*) FROM moment WHERE deleted=false
            """, nativeQuery = true)
    Long count();

    @Query(value = """
            SELECT EXISTS (
              SELECT 1 FROM moment WHERE deleted=false AND blog_domain_name=:blogDomainName AND description=:description
            )
            """, nativeQuery = true)
    boolean existsByBlogDomainNameAndDescription(@Param("blogDomainName") String blogDomainName,
                                                 @Param("description") String description);

    @Modifying
    @Query(value = """
            INSERT INTO moment (blog_domain_name, description, image_url, created_at, updated_at, deleted)
            VALUES (:blogDomainName, :description, :imageURL, NOW(), NOW(), false)
            """, nativeQuery = true)
    void save(@Param("blogDomainName") String blogDomainName,
              @Param("description") String description,
              @Param("imageURL") String imageURL);
}
