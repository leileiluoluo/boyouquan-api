package com.boyouquan.repository;

import com.boyouquan.model.PostImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends CrudRepository<PostImage, Long> {

    @Query(value = """
            SELECT CONCAT(year_str, '/', month_str, '/', image_name)
            FROM post_image
            WHERE deleted=false AND link=:link
            """, nativeQuery = true)
    String getImageURLByLink(@Param("link") String link);

    PostImage findByLink(String link);

    @Query(value = """
            SELECT EXISTS (SELECT 1 FROM post_image WHERE deleted=false AND link=:link)
            """, nativeQuery = true)
    boolean existsImageURLByLink(@Param("link") String link);

    @Modifying
    @Query(value = """
            INSERT INTO post_image (link, image_type, raw_image_url, raw_image_size_kb, raw_image_width, raw_image_height,
                                    image_size_kb, image_width, image_height, year_str, month_str, image_name,
                                    captured_at, updated_at, deleted)
            VALUES (:link, :imageType, :rawImageURL, :rawImageSizeKb, :rawImageWidth, :rawImageHeight,
                    :imageSizeKb, :imageWidth, :imageHeight, :yearStr, :monthStr, :imageName, NOW(), NOW(), false)
            """, nativeQuery = true)
    void save(@Param("link") String link,
              @Param("imageType") String imageType,
              @Param("rawImageURL") String rawImageURL,
              @Param("rawImageSizeKb") Integer rawImageSizeKb,
              @Param("rawImageWidth") Integer rawImageWidth,
              @Param("rawImageHeight") Integer rawImageHeight,
              @Param("imageSizeKb") Integer imageSizeKb,
              @Param("imageWidth") Integer imageWidth,
              @Param("imageHeight") Integer imageHeight,
              @Param("yearStr") String yearStr,
              @Param("monthStr") String monthStr,
              @Param("imageName") String imageName);

    @Modifying
    @Query(value = """
            UPDATE post_image
            SET image_type=:imageType,
                raw_image_url=:rawImageURL,
                raw_image_size_kb=:rawImageSizeKb,
                raw_image_width=:rawImageWidth,
                raw_image_height=:rawImageHeight,
                image_size_kb=:imageSizeKb,
                image_width=:imageWidth,
                image_height=:imageHeight,
                year_str=:yearStr,
                month_str=:monthStr,
                image_name=:imageName,
                updated_at=NOW()
            WHERE link=:link
            """, nativeQuery = true)
    void update(@Param("link") String link,
                @Param("imageType") String imageType,
                @Param("rawImageURL") String rawImageURL,
                @Param("rawImageSizeKb") Integer rawImageSizeKb,
                @Param("rawImageWidth") Integer rawImageWidth,
                @Param("rawImageHeight") Integer rawImageHeight,
                @Param("imageSizeKb") Integer imageSizeKb,
                @Param("imageWidth") Integer imageWidth,
                @Param("imageHeight") Integer imageHeight,
                @Param("yearStr") String yearStr,
                @Param("monthStr") String monthStr,
                @Param("imageName") String imageName);
}
