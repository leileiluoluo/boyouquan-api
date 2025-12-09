package com.boyouquan.dao;

import com.boyouquan.model.Moment;

import java.util.Date;
import java.util.List;

public interface MomentDaoMapper {

    List<Moment> list(int offset, int rows);

    Long count();

    List<Moment> listByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    Long countByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    Long countLikesByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    boolean existsByBlogDomainNameAndDescription(String blogDomainName, String description);

    void save(Moment moment);

}
