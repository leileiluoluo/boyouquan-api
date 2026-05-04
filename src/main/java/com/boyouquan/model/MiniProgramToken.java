package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MiniProgramToken {

    @JsonProperty("access_token")
    private String accessToken;

}
