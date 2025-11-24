package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;

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
