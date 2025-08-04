package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.MonthlySelectedDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.MonthlySelectedPost;
import com.boyouquan.model.Post;
import com.boyouquan.model.SelectedPostAccess;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MonthlySelectedServiceImpl implements MonthlySelectedService {

    @Autowired
    private MonthlySelectedDaoMapper monthlySelectedDaoMapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogStatusService blogStatusService;
    @Autowired
    private EmailService emailService;

    @Override
    public List<String> listYearMonthStrs() {
        return monthlySelectedDaoMapper.listYearMonthStrs();
    }

    @Override
    public MonthlySelectedPost listSelectedByYearMonth(String yearMonth) {
        List<SelectedPostAccess> selectedPostAccessList = monthlySelectedDaoMapper.listSelectedPostsByYearMonthStr(yearMonth, CommonConstants.MONTHLY_SELECTED_POSTS_LIMIT);

        List<MonthlySelectedPost.PostInfoWithBlogStatus> postInfos = selectedPostAccessList.stream()
                .sorted(Comparator.comparing(SelectedPostAccess::getPublishedAt).reversed())
                .map(selectedPostAccess -> {
                    Post post = postService.getByLink(selectedPostAccess.getPostLink());

                    MonthlySelectedPost.PostInfoWithBlogStatus postInfo = new MonthlySelectedPost.PostInfoWithBlogStatus();
                    BeanUtils.copyProperties(post, postInfo);

                    // blog
                    Blog blog = blogService.getByDomainName(post.getBlogDomainName());
                    postInfo.setBlogName(blog.getName());
                    postInfo.setBlogAddress(blog.getAddress());

                    // blog status
                    boolean blogStatusOk = blogStatusService.isStatusOkByBlogDomainName(selectedPostAccess.getBlogDomainName());
                    postInfo.setBlogStatusOk(blogStatusOk);

                    return postInfo;
                }).toList();

        MonthlySelectedPost monthlySelectedPost = new MonthlySelectedPost();
        monthlySelectedPost.setYearMonthStr(yearMonth);
        monthlySelectedPost.setPostInfos(postInfos);

        return monthlySelectedPost;
    }

    @Override
    public void sendLatestReport(String email) {
        String latestYearMonth = CommonUtils.getYearMonthStr(new Date());
        MonthlySelectedPost monthlySelectedPost = listSelectedByYearMonth(latestYearMonth);

        // send
        emailService.sendLatestMonthlySelected(email, monthlySelectedPost);
    }

}
