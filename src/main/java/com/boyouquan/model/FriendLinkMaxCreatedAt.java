package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@Builder
@Data
public class FriendLinkMaxCreatedAt {

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date maxCreatedAt;

}
