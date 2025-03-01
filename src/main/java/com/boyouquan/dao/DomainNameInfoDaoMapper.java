package com.boyouquan.dao;

import com.boyouquan.model.DomainNameInfo;

public interface DomainNameInfoDaoMapper {

    DomainNameInfo getByRealDomainName(String realDomainName);

    void save(DomainNameInfo domainNameInfo);

    void update(DomainNameInfo domainNameInfo);

}
