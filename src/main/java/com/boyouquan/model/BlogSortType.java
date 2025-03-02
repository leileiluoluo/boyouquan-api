package com.boyouquan.model;

public enum BlogSortType {
    collect_time,
    access_count,
    create_time;

    public static BlogSortType of(String sort) {
        for (BlogSortType sortType : BlogSortType.values()) {
            if (sortType.name().equals(sort)) {
                return sortType;
            }
        }
        return collect_time;
    }
}
