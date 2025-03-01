package com.boyouquan.service;

import com.boyouquan.model.DomainNameInfo;

public interface DomainNameInfoService {

    DomainNameInfo getDomainNameInfoByBlogDomainName(String blogDomainName);

    void refreshDomainNameInfo(String blogDomainName);

}
