package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class Moment {

    private Long id;
    private String blogDomainName;
    private String description;
    private String imageURL;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;

}
