package com.boyouquan.service;

import com.boyouquan.model.PostDetail;

public interface PostDetailService {

    boolean existsByLink(String link);

    PostDetail getByBlogDomainNameAndLink(String blogDomainName, String link);

    PostDetail extractAndSave(String blogDomainName, String link);

    void updateContentByLink(String link, String content);

}
