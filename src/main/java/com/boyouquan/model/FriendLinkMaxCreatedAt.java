package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class FriendLinkMaxCreatedAt {

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date maxCreatedAt;

}
