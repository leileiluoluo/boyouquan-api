package com.boyouquan.service.impl;

import com.boyouquan.dao.MomentDaoMapper;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.MomentService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MomentServiceImpl implements MomentService {

    @Autowired
    private MomentDaoMapper momentDaoMapper;
    @Autowired
    private BlogService blogService;

    @Override
    public Pagination<MomentInfo> listMomentInfos(int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Moment> moments = momentDaoMapper.list(offset, size);
        Long total = momentDaoMapper.count();
        List<MomentInfo> momentInfos = new ArrayList<>();

        for (Moment moment : moments) {
            // assemble
            MomentInfo momentInfo = new MomentInfo();
            BeanUtils.copyProperties(moment, momentInfo);
            BlogInfo blogInfo = blogService.getBlogInfoByDomainName(moment.getBlogDomainName());
            momentInfo.setBlogInfo(blogInfo);
            momentInfos.add(momentInfo);
        }

        return PaginationBuilder.<MomentInfo>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(momentInfos).build();
    }

    @Override
    public void save(Moment moment) {
        momentDaoMapper.save(moment);
    }

}
