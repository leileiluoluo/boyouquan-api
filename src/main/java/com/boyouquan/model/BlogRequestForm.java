package com.boyouquan.model;

import lombok.Data;


@Data
public class BlogRequestForm {

    private String name;
    private String description;
    private String rssAddress;
    private String adminEmail;
    private String emailVerificationCode;

}
