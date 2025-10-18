package com.boyouquan.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BlogIntimacy {

    List<FriendLinkInfo> path;

}
