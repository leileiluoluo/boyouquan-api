package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Post implements Serializable {
    @Serial
    private static final long serialVersionUID = -1412983129684476980L;

    private String link;
    private String blogDomainName;
    private String title;
    private String description;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date publishedAt;
    private Boolean draft = false;
    private Boolean recommended = false;
    private Boolean pinned = false;

}
