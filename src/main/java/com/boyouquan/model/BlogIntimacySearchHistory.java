package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class BlogIntimacySearchHistory {

    private Long id;
    private String sourceBlogDomainName;
    private String targetBlogDomainName;
    private Integer pathLength;
    @JsonIgnore
    private String ipAddress;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date searchedAt;

}
