package com.boyouquan.model;

import lombok.Data;

import java.util.Date;

@Data
public class PopularBlog {

    private String blogDomainName;
    private Date latestActiveTime;

}
