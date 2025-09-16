package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MonthlySelectedPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 7447207644412586281L;

    private String yearMonthStr;
    private List<PostInfoWithBlogStatus> postInfos;

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class PostInfoWithBlogStatus extends PostInfo {
        @Serial
        private static final long serialVersionUID = -3112386258677953078L;

        private boolean blogStatusOk;
        private boolean hasImage;
        private String imageURL;
    }
}
