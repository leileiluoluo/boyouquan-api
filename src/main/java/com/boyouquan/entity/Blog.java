package com.boyouquan.entity;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @Column(name = "domain_name")
    private String domainName;

    @JsonIgnore
    @Column(name = "admin_email")

    private String adminEmail;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "rss_address")
    private String rssAddress;
    @Column(name = "description")
    private String description;
    @Column(name = "self_submitted")
    private Boolean selfSubmitted;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "collected_at")
    private Date collectedAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "draft")
    private Boolean draft;
    @Column(name = "gravatar_valid")
    private Boolean gravatarValid;
    @Column(name = "valid")
    private Boolean valid;
    @Column(name = "deleted")
    protected Boolean deleted;

}
