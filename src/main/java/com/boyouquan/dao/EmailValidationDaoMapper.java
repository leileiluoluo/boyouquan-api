package com.boyouquan.dao;

import com.boyouquan.model.EmailValidation;

public interface EmailValidationDaoMapper {

    EmailValidation getByEmailAndCode(String email, String code);

    int countTodayIssuedByEmail(String email);

    void save(EmailValidation emailValidation);

}
