package com.boyouquan.dao;

import com.boyouquan.model.SelectedPostAccess;

import java.util.List;

public interface MonthlySelectedDaoMapper {

    List<String> listYearMonthStrs(boolean includeCurrentMonth);

    List<SelectedPostAccess> listSelectedPostsByYearMonthStr(String yearMonthStr, int limit);

}
