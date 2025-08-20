package com.boyouquan.service.impl;

import com.boyouquan.dao.DomainNameChangeDaoMapper;
import com.boyouquan.model.DomainNameChange;
import com.boyouquan.service.DomainNameChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainNameChangeServiceImpl implements DomainNameChangeService {

    @Autowired
    private DomainNameChangeDaoMapper domainNameChangeDaoMapper;

    @Override
    public boolean existsByOldDomainName(String oldDomainName) {
        return domainNameChangeDaoMapper.existsByOldDomainName(oldDomainName);
    }

    @Override
    public DomainNameChange getByOldDomainName(String oldDomainName) {
        return domainNameChangeDaoMapper.getByOldDomainName(oldDomainName);
    }

    @Override
    public void save(DomainNameChange domainNameChange) {
        domainNameChangeDaoMapper.save(domainNameChange);
    }

}
