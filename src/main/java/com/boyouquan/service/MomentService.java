package com.boyouquan.service;

import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.util.Pagination;

import java.util.Date;
import java.util.List;

public interface MomentService {

    Pagination<MomentInfo> listMomentInfos(int page, int size);

    boolean existsByBlogDomainNameAndDescription(String blogDomainName, String description);

    List<Moment> listByBlogDomainName(String blogDomainName, Date startDate);

    Long countByBlogDomainName(String blogDomainName, Date startDate);

    Long countLikesByBlogDomainName(String blogDomainName, Date startDate);

    void save(Moment moment);

}
