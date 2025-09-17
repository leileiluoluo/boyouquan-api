package com.boyouquan.service;

import java.util.List;

public interface ImageScraperService {

    List<String> getImages(String url, int limit);

}
