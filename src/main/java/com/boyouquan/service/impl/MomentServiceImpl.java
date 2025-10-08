package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.MomentDaoMapper;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.LikeService;
import com.boyouquan.service.MomentService;
import com.boyouquan.service.WebSocketService;
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
    @Autowired
    private LikeService likeService;
    @Autowired
    private WebSocketService webSocketService;

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
            Long likeCount = likeService.countByTypeAndEntityId(Like.Type.MOMENTS, moment.getId());
            momentInfo.setLikeCount(likeCount);
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

        // websocket broadcast
        String blogDomainName = moment.getBlogDomainName();
        Blog blog = blogService.getByDomainName(blogDomainName);
        if (null != blog) {
            WebSocketMessage message = new WebSocketMessage();
            message.setMessage(String.format("「%s」刚刚发布了一条随手一拍，快来看看吧！", blog.getName()));
            message.setGotoUrl(CommonConstants.MOMENTS_PAGE_ADDRESS);
            webSocketService.broadcast(message);
        }
    }

}
