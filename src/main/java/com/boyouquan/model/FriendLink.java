package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class FriendLink {

    private Long id;
    private String sourceBlogDomainName;
    private String targetBlogDomainName;
    private String pageTitle;
    private String pageUrl;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;

}
