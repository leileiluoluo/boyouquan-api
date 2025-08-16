package com.boyouquan.model;

import lombok.Data;

@Data
public class PlanetShuttleGo {

    private String blogName;
    private String blogAddress;
    private String blogDescription;
    private Blog fromBlog;
    private Long fromBlogInitiatedCount;

}
