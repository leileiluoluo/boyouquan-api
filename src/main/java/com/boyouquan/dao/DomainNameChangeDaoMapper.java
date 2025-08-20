package com.boyouquan.dao;

import com.boyouquan.model.DomainNameChange;

public interface DomainNameChangeDaoMapper {

    boolean existsByOldDomainName(String oldDomainName);

    DomainNameChange getByOldDomainName(String oldDomainName);

    void save(DomainNameChange domainNameChange);

}
