package com.boyouquan.model;

import lombok.Data;

import java.util.Date;

@Data
public class DomainNameInfo {

    private String blogDomainName;
    private String realDomainName;
    private Date registeredAt;
    private Date createdAt;
    private Date updatedAt;
    protected Boolean deleted;

}
