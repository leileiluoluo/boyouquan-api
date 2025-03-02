package com.boyouquan.service.impl;

import com.boyouquan.dao.DomainNameInfoDaoMapper;
import com.boyouquan.helper.WhoisInfoHelper;
import com.boyouquan.model.DomainNameInfo;
import com.boyouquan.model.WhoisInfo;
import com.boyouquan.service.DomainNameInfoService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainNameInfoServiceImpl implements DomainNameInfoService {

    @Autowired
    private WhoisInfoHelper whoisInfoHelper;
    @Autowired
    private DomainNameInfoDaoMapper domainNameInfoDaoMapper;

    @Override
    public DomainNameInfo getDomainNameInfoByBlogDomainName(String blogDomainName) {
        return domainNameInfoDaoMapper.getByBlogDomainName(blogDomainName);
    }

    @Override
    public void refreshDomainNameInfo(String blogDomainName) {
        String realDomainName = CommonUtils.getRealWhoisDomainFromBlogDomainName(blogDomainName);

        WhoisInfo whoisInfo = whoisInfoHelper.getDomainNameInfoByRealDomainName(realDomainName);
        if (null != whoisInfo) {
            DomainNameInfo existingDomainNameInfo = domainNameInfoDaoMapper.getByBlogDomainName(blogDomainName);
            if (null != existingDomainNameInfo) {
                existingDomainNameInfo.setRegisteredAt(whoisInfo.getCreated());
                domainNameInfoDaoMapper.update(existingDomainNameInfo);
            } else {
                DomainNameInfo domainNameInfo = new DomainNameInfo();
                domainNameInfo.setBlogDomainName(blogDomainName);
                domainNameInfo.setRealDomainName(realDomainName);
                domainNameInfo.setRegisteredAt(whoisInfo.getCreated());
                domainNameInfoDaoMapper.save(domainNameInfo);
            }
        }
    }
}
