package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostInfo extends Post {

    @Serial
    private static final long serialVersionUID = -1043788671013195608L;

    private String blogName;
    private String blogAddress;
    private boolean blogStatusOk;
    private Long linkAccessCount;
    private String blogAdminLargeImageURL;

}
