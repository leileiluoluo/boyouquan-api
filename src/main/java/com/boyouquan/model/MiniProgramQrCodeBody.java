package com.boyouquan.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MiniProgramQrCodeBody {

    private String path;
    private int width;

}
