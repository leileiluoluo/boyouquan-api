package com.boyouquan.service.impl;

import com.boyouquan.dao.EmailValidationDaoMapper;
import com.boyouquan.model.EmailValidation;
import com.boyouquan.service.EmailService;
import com.boyouquan.service.EmailValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {

    @Autowired
    private EmailValidationDaoMapper emailValidationDaoMapper;
    @Autowired
    private EmailService emailService;

    @Override
    public EmailValidation getByEmailAndCode(String email, String code) {
        return emailValidationDaoMapper.getByEmailAndCode(email, code);
    }

    @Override
    public int countTodayIssuedByEmail(String email) {
        return emailValidationDaoMapper.countTodayIssuedByEmail(email);
    }

    @Override
    public void generateCodeSendEmailAndSave(String email) {
        String code = generateCode();

        // send email
        emailService.sendEmailValidationCode(email, code);

        // save
        EmailValidation emailValidation = new EmailValidation();
        emailValidation.setEmail(email);
        emailValidation.setCode(code);
        emailValidationDaoMapper.save(emailValidation);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
