package com.boyouquan.dao;

import com.boyouquan.model.DomainNameInfo;

public interface DomainNameInfoDaoMapper {

    DomainNameInfo getByBlogDomainName(String blogDomainName);

    void save(DomainNameInfo domainNameInfo);

    void update(DomainNameInfo domainNameInfo);

}
