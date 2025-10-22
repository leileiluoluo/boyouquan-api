package com.boyouquan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BlogIntimacySearchHistoryInfo extends BlogIntimacySearchHistory {

    private String title;
    private String link;

}
