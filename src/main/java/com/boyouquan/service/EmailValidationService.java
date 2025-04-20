package com.boyouquan.service;

import com.boyouquan.model.EmailValidation;

public interface EmailValidationService {

    EmailValidation getByEmailAndCode(String email, String code);

    int countTodayIssuedByEmail(String email);

    void generateCodeSendEmailAndSave(String email);

}
