package com.boyouquan.service;

import com.boyouquan.model.DomainNameChange;

public interface DomainNameChangeService {

    boolean existsByOldDomainName(String oldDomainName);

    DomainNameChange getByOldDomainName(String oldDomainName);

    void save(DomainNameChange domainNameChange);

}
