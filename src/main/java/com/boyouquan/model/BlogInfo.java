package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogInfo extends Blog {

    private Long postCount;
    private Long accessCount;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date latestPublishedAt;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date domainNameRegisteredAt;
    private List<Post> posts = Collections.emptyList();
    private String blogAdminMediumImageURL;
    private String blogAdminLargeImageURL;
    private String submittedInfo;
    private String submittedInfoTip;
    private boolean statusOk;
    private boolean sunset;
    private String statusUnOkInfo;
    private String blogServerLocation;

}
