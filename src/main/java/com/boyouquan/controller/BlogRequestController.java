package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.EmailValidationService;
import com.boyouquan.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/blog-requests")
public class BlogRequestController {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;
    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private IPControlHelper ipControlHelper;
    @Autowired
    private EmailValidationService emailValidationService;

    @GetMapping("")
    public ResponseEntity<Pagination<BlogRequestInfo>> listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "onlySelfSubmitted", required = false, defaultValue = "true") boolean onlySelfSubmitted) {
        List<BlogRequest.Status> statuses = Arrays.asList(
                BlogRequest.Status.submitted,
                BlogRequest.Status.system_check_valid,
                BlogRequest.Status.system_check_invalid,
                BlogRequest.Status.approved,
                BlogRequest.Status.rejected,
                BlogRequest.Status.uncollected
        );

        Pagination<BlogRequestInfo> blogRequestInfo = PaginationBuilder.buildEmptyResults();
        if (onlySelfSubmitted) {
            blogRequestInfo = blogRequestService.listBlogRequestInfosBySelfSubmittedAndStatuses(keyword, true, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);
        } else {
            blogRequestInfo = blogRequestService.listBlogRequestInfosByStatuses(
                    keyword, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);
        }

        return ResponseEntity.ok(blogRequestInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogRequestById(@PathVariable("id") Long id) {
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        if (null == blogRequestInfo) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_NOT_EXISTS);
        }

        return ResponseEntity.ok(blogRequestInfo);
    }

    @PostMapping("/validation-code")
    public ResponseEntity<?> sendEmailValidationCode(@RequestBody EmailValidationForm emailValidationForm) {
        // email
        String adminEmail = emailValidationForm.getAdminEmail().trim();
        if (!EmailUtil.isEmailValid(adminEmail)) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_ADMIN_EMAIL_INVALID);
        }

        // check
        int count = emailValidationService.countTodayIssuedByEmail(adminEmail);
        if (count >= CommonConstants.EMAIL_VALIDATION_CODE_ONE_DAY_LIMIT) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_EMAIL_VALIDATION_CODE_LIMIT_EXCEED);
        }

        // generate code & send email
        emailValidationService.generateCodeSendEmailAndSave(adminEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email-validation")
    public ResponseEntity<?> addBlogRequest(@RequestBody EmailValidationForm emailValidationForm) {
        // email
        String adminEmail = emailValidationForm.getAdminEmail().trim();
        if (!EmailUtil.isEmailValid(adminEmail)) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_ADMIN_EMAIL_INVALID);
        }

        // email validation
        EmailValidation emailValidation = emailValidationService.getByEmailAndCode(adminEmail, emailValidationForm.getEmailVerificationCode());
        if (null == emailValidation
                || CommonUtils.isDateOneHourAgo(emailValidation.getIssuedAt())) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_EMAIL_VALIDATION_CODE_INVALID);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("")
    public ResponseEntity<?> addBlogRequest(@RequestBody BlogRequestForm blogRequestForm, HttpServletRequest request) {
        ErrorCode error = blogRequestFormHelper.paramsValidation(blogRequestForm);
        if (null != error) {
            return ResponseUtil.errorResponse(error);
        }

        // email validation
        EmailValidation emailValidation = emailValidationService.getByEmailAndCode(blogRequestForm.getAdminEmail(), blogRequestForm.getEmailVerificationCode());
        if (null == emailValidation
                || CommonUtils.isDateOneHourAgo(emailValidation.getIssuedAt())) {
            return ResponseUtil.errorResponse(ErrorCode.BLOG_REQUEST_EMAIL_VALIDATION_CODE_INVALID);
        }

        // submit
        BlogRequest blogRequest = new BlogRequest();
        blogRequest.setName(blogRequestForm.getName().trim());
        blogRequest.setDescription(blogRequestForm.getDescription().trim());
        blogRequest.setRssAddress(blogRequestForm.getRssAddress().trim());
        blogRequest.setAdminEmail(blogRequestForm.getAdminEmail().trim());
        blogRequest.setSelfSubmitted(true);
        blogRequestService.submit(blogRequest);

        executorService.execute(() -> {
            blogRequestService.processNewRequest(blogRequest.getRssAddress());
        });

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
