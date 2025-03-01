package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhoisInfo {

    @JsonProperty("module")
    private Module module;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Module {
        @JsonProperty("creationDate")
        private Date creationDate;
    }

}
