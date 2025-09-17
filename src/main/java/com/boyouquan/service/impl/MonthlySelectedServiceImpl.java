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

import java.util.Calendar;
import java.util.Comparator;
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
    private PostImageService postImageService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private EmailService emailService;

    @Override
    public List<String> listYearMonthStrs(boolean includeCurrentMonth) {
        return monthlySelectedDaoMapper.listYearMonthStrs(includeCurrentMonth);
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

                    // post image
                    boolean existsPostImage = postImageService.existsImageURLByLink(post.getLink());
                    String postImageURL = postImageService.getImageURLByLink(post.getLink());
                    postInfo.setHasImage(existsPostImage);
                    postInfo.setImageURL(postImageURL);

                    // admin image url
                    String blogAdminMediumImageURL = blogService.getBlogAdminMediumImageURLByDomainName(blog.getDomainName());
                    postInfo.setBlogAdminMediumImageURL(blogAdminMediumImageURL);

                    // access count
                    Long linkAccessCount = accessService.countByLink(post.getLink());
                    postInfo.setLinkAccessCount(linkAccessCount);

                    return postInfo;
                }).toList();

        MonthlySelectedPost monthlySelectedPost = new MonthlySelectedPost();
        monthlySelectedPost.setYearMonthStr(yearMonth);
        monthlySelectedPost.setPostInfos(postInfos);

        return monthlySelectedPost;
    }

    @Override
    public void sendLatestReport(String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String latestYearMonth = CommonUtils.getYearMonthStr(calendar.getTime());
        MonthlySelectedPost monthlySelectedPost = listSelectedByYearMonth(latestYearMonth);

        // send
        emailService.sendLatestMonthlySelected(email, monthlySelectedPost);
    }

}
