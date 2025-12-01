package com.boyouquan.repository;

import com.boyouquan.model.PostImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface PostImageRepository extends Repository<Object, Long> {

    @Query(value = "SELECT concat(year_str, '/', month_str, '/', image_name) FROM post_image WHERE deleted=false AND link=:link", nativeQuery = true)
    String getImageURLByLink(@Param("link") String link);

    @Query(value = "SELECT link, image_type as imageType, raw_image_url as rawImageURL, raw_image_size_kb as rawImageSizeKb, raw_image_width as rawImageWidth, raw_image_height as rawImageHeight, image_size_kb as imageSizeKb, image_width as imageWidth, image_height as imageHeight, year_str as yearStr, month_str as monthStr, image_name as imageName, captured_at as capturedAt, updated_at as updatedAt, deleted FROM post_image WHERE deleted=false AND link=:link", nativeQuery = true)
    PostImage getByLink(@Param("link") String link);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM post_image WHERE deleted=false AND link=:link)", nativeQuery = true)
    boolean existsImageURLByLink(@Param("link") String link);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO post_image (link, image_type, raw_image_url, raw_image_size_kb, raw_image_width, raw_image_height, image_size_kb, image_width, image_height, year_str, month_str, image_name, captured_at, updated_at, deleted) VALUES (:link, :imageType, :rawImageURL, :rawImageSizeKb, :rawImageWidth, :rawImageHeight, :imageSizeKb, :imageWidth, :imageHeight, :yearStr, :monthStr, :imageName, now(), now(), false)", nativeQuery = true)
    void save(@Param("link") String link, @Param("imageType") String imageType, @Param("rawImageURL") String rawImageURL, @Param("rawImageSizeKb") Long rawImageSizeKb, @Param("rawImageWidth") Integer rawImageWidth, @Param("rawImageHeight") Integer rawImageHeight, @Param("imageSizeKb") Long imageSizeKb, @Param("imageWidth") Integer imageWidth, @Param("imageHeight") Integer imageHeight, @Param("yearStr") String yearStr, @Param("monthStr") String monthStr, @Param("imageName") String imageName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post_image SET image_type=:imageType, raw_image_url=:rawImageURL, raw_image_size_kb=:rawImageSizeKb, raw_image_width=:rawImageWidth, raw_image_height=:rawImageHeight, image_size_kb=:imageSizeKb, image_width=:imageWidth, image_height=:imageHeight, year_str=:yearStr, month_str=:monthStr, image_name=:imageName, updated_at=now() WHERE link=:link", nativeQuery = true)
    void update(@Param("link") String link, @Param("imageType") String imageType, @Param("rawImageURL") String rawImageURL, @Param("rawImageSizeKb") Long rawImageSizeKb, @Param("rawImageWidth") Integer rawImageWidth, @Param("rawImageHeight") Integer rawImageHeight, @Param("imageSizeKb") Long imageSizeKb, @Param("imageWidth") Integer imageWidth, @Param("imageHeight") Integer imageHeight, @Param("yearStr") String yearStr, @Param("monthStr") String monthStr, @Param("imageName") String imageName);
}
