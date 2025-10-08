package com.boyouquan.model;

import lombok.Data;

@Data
public class Like {

    private String yearMonthStr;
    private Type type;
    private Long entityId;
    private Long amount;

    public enum Type {
        MOMENTS
    }

}
