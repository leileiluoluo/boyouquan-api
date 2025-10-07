package com.boyouquan.util;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.ImageCompressResult;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageFileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileUtils.class);

    public static String getFileExtension(String imageURL) {
        if (imageURL.contains(".")) {
            String urlPath = imageURL.split("\\?")[0];
            String[] parts = urlPath.split("\\.");
            if (parts.length > 1) {
                String potentialExt = "." + parts[parts.length - 1].toLowerCase();
                if (potentialExt.matches("\\.(jpg|jpeg|png|svg|webp)$")) {
                    return potentialExt;
                }
            }
        }
        return null;
    }

    public static String generateFileName(String extension) {
        return "image_" + UUID.randomUUID().toString().substring(0, 8) + extension;
    }

    public static ImageCompressResult compressToNewFile(String outputPath, Path sourceFile, long originalTotalBytes, String fileExtension) {
        try {
            String newFileName = ImageFileUtils.generateFileName(fileExtension);
            Path newOutputFile = Paths.get(outputPath, newFileName);
            int originalImageWidth = getImageWidth(sourceFile.toString());
            int originalImageHeight = getImageHeight(sourceFile.toString());
            int imageWidth = originalImageWidth;
            int imageHeight = originalImageHeight;
            if (originalTotalBytes / 1000 > CommonConstants.POST_IMAGES_SIZE_LIMIT) {
                if (originalImageWidth > CommonConstants.POST_IMAGES_WIDTH_LIMIT) {
                    imageWidth = CommonConstants.POST_IMAGES_WIDTH_LIMIT;
                    imageHeight = originalImageHeight * CommonConstants.POST_IMAGES_WIDTH_LIMIT / originalImageWidth;
                }

                long compressedTotalBytes = compressImage(fileExtension, sourceFile.toString(), newOutputFile.toString(), imageWidth, imageHeight);
                if (compressedTotalBytes > 0L) {
                    return ImageCompressResult.builder()
                            .success(true)
                            .originalFilePath(sourceFile.toString())
                            .originalSizeKb(originalTotalBytes / 1000)
                            .originalImageWidth(originalImageWidth)
                            .originalImageHeight(originalImageHeight)
                            .fileName(newFileName)
                            .sizeKb(compressedTotalBytes / 1000)
                            .imageWidth(imageWidth)
                            .imageHeight(imageHeight)
                            .build();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (Files.exists(sourceFile)) {
                    Files.delete(sourceFile);
                    LOGGER.info("original file has been deleted, file: {}", sourceFile);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return ImageCompressResult.builder().success(false).build();
    }

    private static int getImageWidth(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            return image.getWidth();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }

    private static int getImageHeight(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            return image.getHeight();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }

    private static long compressImage(String fileExtension, String inputPath, String outputPath, int width, int height) {
        try {
            File outputFile = new File(outputPath);

            BufferedImage originalImage = ImageIO.read(new File(inputPath));
            BufferedImage thumbnail = Thumbnails.of(originalImage)
                    .size(width, height)
                    .asBufferedImage();

            String format = fileExtension.replace(".", "");
            if ("webp".equals(format)) {
                format = "jpg";
            }
            ImageIO.write(thumbnail, format, outputFile);

            return outputFile.length();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return 0L;
        }
    }

}
