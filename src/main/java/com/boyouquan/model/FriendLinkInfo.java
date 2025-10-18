package com.boyouquan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FriendLinkInfo extends FriendLink {

    private BlogInfo sourceBlog;
    private BlogInfo targetBlog;

}
