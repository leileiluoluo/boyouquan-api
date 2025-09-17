package com.boyouquan.service;

import com.boyouquan.model.MonthlySelectedPost;

import java.util.List;

public interface MonthlySelectedService {

    List<String> listYearMonthStrs(boolean includeCurrentMonth);

    MonthlySelectedPost listSelectedByYearMonth(String yearMonth);

    void sendLatestReport(String email);

}
