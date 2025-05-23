package com.boyouquan.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pagination<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 9181965185143925494L;

    private int pageNo = 0;
    private int pageSize = 0;
    private long total = 0L;
    private List<T> results = new ArrayList<>();

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return this.total;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public List<T> getResults() {
        return this.results;
    }

    public boolean hasNextPage() {
        return total > (long) pageNo * pageSize;
    }

}
