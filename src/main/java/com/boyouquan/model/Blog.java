package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {

    private String domainName;
    @JsonIgnore
    private String adminEmail;
    private String name;
    private String address;
    private String rssAddress;
    private String description;
    private Boolean selfSubmitted;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date collectedAt;
    private Date updatedAt;
    private Boolean draft;
    private Boolean gravatarValid;
    private Boolean valid;
    protected Boolean deleted;

}
