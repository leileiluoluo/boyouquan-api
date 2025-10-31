package com.boyouquan.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BlogIntimacy {

    private BlogInfo sourceBlog;
    private BlogInfo targetBlog;
    List<FriendLinkInfo> path;

}
