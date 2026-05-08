package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPLocationInfo {

    @JsonProperty("ret")
    private Integer status;
    private IpData data;

    public String getLocationInfo() {
        String info = null;
        if (null == this.data) {
            return null;
        }

        if ("中国".equals(this.data.country)) {
            if (this.data.province.equals(this.data.city)) {
                info = this.data.province;
            } else {
                info = this.data.province + this.data.city;
            }
        } else {
            info = this.data.country;
        }

        if (StringUtils.isNotBlank(this.data.isp)) {
            info += "（" + this.data.isp + "）";
        }

        return info;
    }

    @Data
    public static class IpData {
        private String country;
        @JsonProperty("prov")
        private String province;
        private String city;
        private String isp;
    }

}
