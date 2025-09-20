package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.BlogService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogRequestFormHelper {

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private BlogService blogService;

    public ErrorCode paramsValidation(BlogRequestForm blogRequestForm) {
        // name
        if (StringUtils.isBlank(blogRequestForm.getName())) {
            return ErrorCode.BLOG_REQUEST_NAME_INVALID;
        }
        String name = blogRequestForm.getName().trim();
        if (name.length() > 20) {
            return ErrorCode.BLOG_REQUEST_NAME_INVALID;
        }

        // description
        if (StringUtils.isBlank(blogRequestForm.getDescription())) {
            return ErrorCode.BLOG_REQUEST_DESCRIPTION_INVALID;
        }
        String description = blogRequestForm.getDescription().trim();
        if (description.length() < 10 || description.length() > 300) {
            return ErrorCode.BLOG_REQUEST_DESCRIPTION_INVALID;
        }

        // rss address
        if (StringUtils.isBlank(blogRequestForm.getRssAddress())) {
            return ErrorCode.BLOG_REQUEST_RSS_ADDRESS_INVALID;
        }
        String rssAddress = blogRequestForm.getRssAddress().trim();
        if (!rssAddress.startsWith("http")) {
            return ErrorCode.BLOG_REQUEST_RSS_ADDRESS_INVALID;
        }
        if (!CommonUtils.getNoSchemeDomainName(rssAddress).contains("/")) {
            return ErrorCode.BLOG_REQUEST_RSS_ADDRESS_INVALID;
        }

        // refusing domains check
        String domain = CommonUtils.getDomainFromURL(rssAddress);
        if (boYouQuanConfig.getDomainsRefuseToJoin().stream().anyMatch(domain::endsWith)) {
            return ErrorCode.BLOG_REQUEST_RSS_ADDRESS_BLACK_LIST;
        }

        // email
        if (StringUtils.isBlank(blogRequestForm.getAdminEmail())) {
            return ErrorCode.BLOG_REQUEST_ADMIN_EMAIL_INVALID;
        }
        String adminEmail = blogRequestForm.getAdminEmail().trim();
        if (!EmailUtil.isEmailValid(adminEmail)) {
            return ErrorCode.BLOG_REQUEST_ADMIN_EMAIL_INVALID;
        }

        // blog request exists?
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            return ErrorCode.BLOG_REQUEST_RSS_ADDRESS_EXISTS;
        }

        // blog exists?
        String domainName = CommonUtils.getDomainName(rssAddress);
        if (blogService.existsByDomainName(domainName)) {
            return ErrorCode.BLOG_REQUEST_BLOG_EXISTS;
        }

        return null;
    }

}
