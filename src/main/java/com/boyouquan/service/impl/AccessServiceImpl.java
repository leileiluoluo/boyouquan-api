package com.boyouquan.service.impl;

import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.entity.Access;
import com.boyouquan.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccessDaoMapper accessDaoMapper;
    @Autowired
    private IPControlHelper ipControlHelper;

    @Override
    public Long countAll() {
        return accessDaoMapper.countAll();
    }

    @Override
    public BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth() {
        return accessDaoMapper.getMostAccessedBlogDomainNameInLastMonth();
    }

    @Override
    public BlogLinkAccess getMostAccessedLinkByDomainName(String domainName, String blogAddress, Date startDate) {
        return accessDaoMapper.getMostAccessedLinkByBlogDomainName(domainName, blogAddress, startDate);
    }

    @Override
    public List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName) {
        return accessDaoMapper.getBlogAccessSeriesInLatestOneYear(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName) {
        return accessDaoMapper.countByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName, Date startDate) {
        return accessDaoMapper.countByBlogDomainNameAndStartDate(blogDomainName, startDate);
    }

    @Override
    public List<BlogAccessCount> listBlogAccessCount(Date startDate) {
        return accessDaoMapper.listBlogAccessCount(startDate);
    }

    @Override
    public Long countByLink(String link) {
        return accessDaoMapper.countByLink(link);
    }

    @Override
    public void save(Access access) {
        String ip = access.getIp();
        String link = access.getLink();

        if (!ipControlHelper.alreadyAccessed(ip, link)) {
            // save
            accessDaoMapper.save(access);
            ipControlHelper.access(ip, link);
        }
    }

    @Override
    public void deleteByBlogDomainName(String blogDomainName) {
        accessDaoMapper.deleteByBlogDomainName(blogDomainName);
    }

}
