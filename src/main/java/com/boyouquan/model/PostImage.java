package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class PostImage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1412983129684476981L;

    private String link;
    private String imageType;
    private String rawImageURL;
    private Long rawImageSizeKb;
    private Long rawImageWidth;
    private Long rawImageHeight;
    private Long imageSizeKb;
    private Long imageWidth;
    private Long imageHeight;
    private String yearStr;
    private String monthStr;
    private String imageName;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date capturedAt;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;
    private Boolean deleted;

}
