package com.boyouquan.service;

import com.boyouquan.model.BlogIntimacySearchHistory;
import com.boyouquan.model.BlogIntimacySearchHistoryInfo;

import java.util.List;

public interface BlogIntimacySearchHistoryService {

    List<BlogIntimacySearchHistoryInfo> listLatestMessages(int size);

    void save(BlogIntimacySearchHistory history);

}
