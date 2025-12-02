package com.boyouquan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AccessPrimaryKey implements Serializable {

    @Column(name = "year_month_str")
    private String yearMonthStr;
    @Column(name = "blog_domain_name")
    private String blogDomainName;
    @Column(name = "link")
    private String link;

}
