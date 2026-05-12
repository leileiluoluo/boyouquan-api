package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Data
public class PostDetail {

    private String link;
    private String blogDomainName;
    private String content;
    private String contentRefined;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;

}
