package com.boyouquan.service.impl;

import com.boyouquan.dao.PostDetailDaoMapper;
import com.boyouquan.model.PostDetail;
import com.boyouquan.service.ArticleExtractorService;
import com.boyouquan.service.PostDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
        return postDetailDaoMapper.getByLink(link);
    }

    @Override
    public PostDetail extractAndSave(String blogDomainName, String link) {
        String content = articleExtractorService.getContent(link);
        if (StringUtils.isBlank(content)) {
            log.error("content is blank, link: {}", link);
            return null;
        }

        PostDetail existingPostDetail = postDetailDaoMapper.getByLink(link);
        if (null != existingPostDetail) {
            log.info("post detail exists, blogDomainName: {}, link: {}", blogDomainName, link);
            postDetailDaoMapper.updateContentByLink(link, content);
            existingPostDetail.setContent(content);
            return existingPostDetail;
        }

        log.info("save new post detail, blogDomainName: {}, link: {}", blogDomainName, link);

        PostDetail postDetail = new PostDetail();
        postDetail.setLink(link);
        postDetail.setBlogDomainName(blogDomainName);
        postDetail.setContent(content);
        postDetailDaoMapper.save(postDetail);
        return postDetail;
    }

    @Override
    public void updateContentByLink(String link, String content) {
        postDetailDaoMapper.updateContentByLink(link, content);
    }

}
