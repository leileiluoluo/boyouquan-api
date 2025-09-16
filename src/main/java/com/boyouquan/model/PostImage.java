package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class PostImage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1412983129684476981L;

    private String link;
    private String blogDomainName;
    private String imageURL;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date capturedAt;
    private Boolean deleted;

}
