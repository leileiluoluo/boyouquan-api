package com.boyouquan.dao;

import com.boyouquan.model.EmailValidation;

public interface EmailValidationDaoMapper {

    EmailValidation getByEmailAndCode(String email, String code);

    void save(EmailValidation emailValidation);

}
