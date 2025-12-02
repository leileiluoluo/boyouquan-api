package com.boyouquan.service;

import com.boyouquan.model.*;
import com.boyouquan.model.entity.Access;

import java.util.Date;
import java.util.List;

public interface AccessService {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth();

    BlogLinkAccess getMostAccessedLinkByDomainName(String domainName, String blogAddress, Date startDate);

    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName, Date startDate);

    List<BlogAccessCount> listBlogAccessCount(Date startDate);

    Long countByLink(String link);

    void save(Access access);

    void deleteByBlogDomainName(String blogDomainName);

}
