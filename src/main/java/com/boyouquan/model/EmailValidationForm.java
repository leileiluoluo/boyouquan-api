package com.boyouquan.model;

import lombok.Data;


@Data
public class EmailValidationForm {

    private String adminEmail;
    private String code;

}
