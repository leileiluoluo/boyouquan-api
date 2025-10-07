package com.boyouquan.dao;

import com.boyouquan.model.Moment;

import java.util.List;

public interface MomentDaoMapper {

    List<Moment> list(int offset, int rows);

    Long count();

    void save(Moment moment);

}
