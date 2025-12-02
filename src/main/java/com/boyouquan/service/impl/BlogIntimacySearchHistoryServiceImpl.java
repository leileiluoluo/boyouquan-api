package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogIntimacySearchHistoryDaoMapper;
import com.boyouquan.model.BlogIntimacySearchHistory;
import com.boyouquan.model.BlogIntimacySearchHistoryInfo;
import com.boyouquan.service.BlogIntimacySearchHistoryService;
import com.boyouquan.service.BlogService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogIntimacySearchHistoryServiceImpl implements BlogIntimacySearchHistoryService {

    @Autowired
    private BlogIntimacySearchHistoryDaoMapper blogIntimacySearchHistoryDaoMapper;
    @Autowired
    private BlogService blogService;

    @Override
    public List<BlogIntimacySearchHistoryInfo> listLatestMessages(int size) {
        List<BlogIntimacySearchHistory> histories = blogIntimacySearchHistoryDaoMapper.listLatest(size);

        List<BlogIntimacySearchHistoryInfo> historyInfos = new ArrayList<>();
        for (BlogIntimacySearchHistory history : histories) {
            BlogIntimacySearchHistoryInfo info = new BlogIntimacySearchHistoryInfo();

            Blog sourceBlog = blogService.getByDomainName(history.getSourceBlogDomainName());
            Blog targetBlog = blogService.getByDomainName(history.getTargetBlogDomainName());

            if (null != sourceBlog && null != targetBlog) {
                String timeStr = CommonUtils.dateFriendlyDisplay(history.getSearchedAt());
                String message = String.format("[%s] 有人探索了「%s」和「%s」的连接度！", timeStr, sourceBlog.getName(), targetBlog.getName());

                String link = String.format(CommonConstants.GRAPH_LINKS_ADDRESS, sourceBlog.getDomainName(), targetBlog.getDomainName());
                info.setTitle(message);
                info.setLink(link);
                historyInfos.add(info);
            }
        }

        return historyInfos;
    }

    @Override
    public void save(BlogIntimacySearchHistory history) {
        blogIntimacySearchHistoryDaoMapper.save(history);
    }

}
