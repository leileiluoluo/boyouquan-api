package com.boyouquan.service.impl;

import com.boyouquan.dao.PostDetailDaoMapper;
import com.boyouquan.model.PostDetail;
import com.boyouquan.service.ArticleExtractorService;
import com.boyouquan.service.PostDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDetailServiceImpl implements PostDetailService {

    @Autowired
    private PostDetailDaoMapper postDetailDaoMapper;
    @Autowired
    private ArticleExtractorService articleExtractorService;

    @Override
    public boolean existsByLink(String link) {
        return postDetailDaoMapper.existsByLink(link);
    }

    @Override
    public PostDetail getByBlogDomainNameAndLink(String blogDomainName, String link) {
        PostDetail postDetail = postDetailDaoMapper.getByLink(link);
        if (null == postDetail) {
            extractAndSave(blogDomainName, link);
        }
        return postDetail;
    }

    @Override
    public void extractAndSave(String blogDomainName, String link) {
        String content = articleExtractorService.getContent(link);
        if (StringUtils.isNotBlank(content)) {
            PostDetail postDetail = new PostDetail();
            postDetail.setLink(link);
            postDetail.setBlogDomainName(blogDomainName);
            postDetail.setContent(content);
            postDetailDaoMapper.save(postDetail);
        }
    }

    @Override
    public void updateContentByLink(String link, String content) {
        postDetailDaoMapper.updateContentByLink(link, content);
    }

}
