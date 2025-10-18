package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class FriendLink {

    private Long id;
    private String sourceBlogDomainName;
    private String targetBlogDomainName;
    private String pageTitle;
    private String pageUrl;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;

    public String getPageTitle() {
        if (StringUtils.isBlank(this.pageTitle)) {
            return this.pageTitle;
        }

        int index = this.pageTitle.indexOf(" ");
        if (index > 0) {
            return this.pageTitle.substring(0, index);
        }
        return this.pageTitle;
    }

}
