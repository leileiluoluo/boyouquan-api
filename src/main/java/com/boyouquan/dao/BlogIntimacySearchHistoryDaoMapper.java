package com.boyouquan.dao;

import com.boyouquan.model.BlogIntimacySearchHistory;

import java.util.List;

public interface BlogIntimacySearchHistoryDaoMapper {

    List<BlogIntimacySearchHistory> listLatest(int size);

    void save(BlogIntimacySearchHistory history);

}
