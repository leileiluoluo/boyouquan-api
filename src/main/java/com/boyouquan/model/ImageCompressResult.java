package com.boyouquan.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
public class ImageCompressResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -1412983129684476982L;

    private boolean success;
    private String originalFilePath;
    private long originalSizeKb;
    private int originalImageWidth;
    private int originalImageHeight;
    private String filePath;
    private long sizeKb;
    private int imageWidth;
    private int imageHeight;

}
