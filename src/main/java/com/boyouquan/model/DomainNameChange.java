package com.boyouquan.model;

import lombok.Data;

import java.util.Date;

@Data
public class DomainNameChange {

    private Long id;
    private String oldDomainName;
    private String newDomainName;
    private Date changedAt;
    private Boolean deleted;

}
