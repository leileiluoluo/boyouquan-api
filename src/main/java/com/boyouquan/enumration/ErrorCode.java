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
    BLOG_REQUEST_EMAIL_VALIDATION_CODE_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "blog_request_email_validation_code_limit_exceed", "今日验证码获取次数已达上限"),
    BLOG_REQUEST_EMAIL_VALIDATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "blog_request_email_validation_code_invalid", "邮箱验证码无效或已超时"),
    BLOG_REQUEST_RSS_ADDRESS_EXISTS(HttpStatus.BAD_REQUEST, "blog_request_rss_address_exists", "RSS 地址已存在"),
    BLOG_REQUEST_BLOG_EXISTS(HttpStatus.BAD_REQUEST, "blog_request_blog_exists", "博客已被收录，请勿重复提交，您可以在博客广场搜索域名来查看"),
    LOGIN_USERNAME_INVALID(HttpStatus.BAD_REQUEST, "login_username_invalid", "账号无效"),
    LOGIN_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_password_invalid", "密码无效"),
    LOGIN_USERNAME_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_username_password_invalid", "账号密码无效"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "无权限访问该接口"),
    BLOG_SUBMITTED_WITH_SAME_IP(HttpStatus.BAD_REQUEST, "blog_submitted_with_same_ip", "同一 IP 同一天内仅可提交一次"),
    SUBSCRIPTION_PARAMS_INVALID(HttpStatus.BAD_REQUEST, "subscription_params_invalid", "订阅参数无效，请返回重新输入！"),
    SUBSCRIPTION_EXISTS(HttpStatus.BAD_REQUEST, "subscription_exists", "您已订阅了该频道的邮件通知，请勿重复订阅！"),
    SUBSCRIPTION_CANCEL_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "subscription_cancel_token_invalid", "取消订阅的令牌无效"),
    SUBSCRIPTION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "subscription_not_exists", "您未订阅任何频道的邮件通知"),
    SUBSCRIPTION_IP_COUNT_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "subscription_ip_count_limit_exceed", "您的 IP 订阅数已超过限制"),
    POST_IMAGE_ADD_FAILED(HttpStatus.BAD_REQUEST, "post_image_add_failed", "文章配图添加失败"),
    IMAGE_UPLOAD_FILE_INVALID(HttpStatus.BAD_REQUEST, "image_upload_invalid", "图片文件无效"),
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "image_upload_failed", "图片文件上传失败"),
    MOMENTS_PARAMS_INVALID(HttpStatus.BAD_REQUEST, "moments_params_invalid", "参数无效，请检查"),
    MOMENTS_EXISTS(HttpStatus.BAD_REQUEST, "moments_exists", "您已发布过同名随拍，请勿重复发布！"),
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
