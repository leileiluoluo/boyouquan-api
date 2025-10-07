package com.boyouquan.service;

import com.boyouquan.model.Moment;
import com.boyouquan.model.MomentInfo;
import com.boyouquan.util.Pagination;

public interface MomentService {

    Pagination<MomentInfo> listMomentInfos(int page, int size);

    void save(Moment moment);

}
