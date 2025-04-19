package com.boyouquan.enumration;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements RestErrorResponse {

    BLOG_REQUEST_NOT_EXISTS(HttpStatus.BAD_REQUEST, "blog_request_not_exists", "博客申请不存在"),
    BLOG_NOT_EXISTS(HttpStatus.BAD_REQUEST, "blog_not_exists", "博客不存在"),
    POST_NOT_EXISTS(HttpStatus.BAD_REQUEST, "post_not_exists", "文章不存在"),
    BLOG_REQUEST_NAME_INVALID(HttpStatus.BAD_REQUEST, "blog_request_name_invalid", "博客名称不能为空，且不可大于 20 个字符"),
    BLOG_REQUEST_DESCRIPTION_INVALID(HttpStatus.BAD_REQUEST, "blog_request_description_invalid", "博客描述不能为空，且需介于 10 到 300 个字符之间"),
    BLOG_REQUEST_RSS_ADDRESS_INVALID(HttpStatus.BAD_REQUEST, "blog_request_rss_address_invalid", "RSS 地址不能为空，且需是一个可访问的地址"),
    BLOG_REQUEST_RSS_ADDRESS_BLACK_LIST(HttpStatus.BAD_REQUEST, "blog_request_rss_address_black_list", "RSS 地址对应的域名拒绝加入"),
    BLOG_REQUEST_ADMIN_EMAIL_INVALID(HttpStatus.BAD_REQUEST, "blog_request_admin_email_invalid", "博主邮箱不能为空，且需是一个可访问的邮箱地址"),
    BLOG_REQUEST_EMAIL_VALIDATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "blog_request_email_validation_code_invalid", "邮箱验证码无效或已超时"),
    BLOG_REQUEST_RSS_ADDRESS_EXISTS(HttpStatus.BAD_REQUEST, "blog_request_rss_address_exists", "RSS 地址已存在"),
    LOGIN_USERNAME_INVALID(HttpStatus.BAD_REQUEST, "login_username_invalid", "账号无效"),
    LOGIN_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_password_invalid", "密码无效"),
    LOGIN_USERNAME_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_username_password_invalid", "账号密码无效"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "无权限访问该接口"),
    BLOG_SUBMITTED_WITH_SAME_IP(HttpStatus.BAD_REQUEST, "blog_submitted_with_same_ip", "同一 IP 同一天内仅可提交一次"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error", "服务器内部错误");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
