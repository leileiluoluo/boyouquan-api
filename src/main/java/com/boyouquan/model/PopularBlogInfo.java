package com.boyouquan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PopularBlogInfo extends PopularBlog {

    private String blogName;
    private String blogAdminLargeImageURL;

}
