package com.boyouquan.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
public class ImageDownloadResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -1412983129684476982L;

    private boolean success;
    private String message;
    private String imageType;
    private String imageName;
    private long totalBytes;
    private ImageCompressResult compressResult;

}
