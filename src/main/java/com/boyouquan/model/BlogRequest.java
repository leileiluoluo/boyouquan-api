package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
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
public class BlogRequest {

    private Long id;
    private String rssAddress;
    private String adminEmail;
    private String name;
    private String description;
    private Boolean selfSubmitted;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date requestedAt;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;
    private Status status;
    private String reason;
    private Boolean deleted;

    public enum Status {
        submitted,
        system_check_valid,
        system_check_invalid,
        approved,
        rejected,
        uncollected
    }

}
