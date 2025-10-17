package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.service.ImageScraperService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageScraperServiceImpl implements ImageScraperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageScraperServiceImpl.class);

    @Override
    public List<String> getImages(String url, int limit) {
        List<String> imageURLs = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url)
                    .header(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                    .get();
            Elements images = doc.select("img[src]");
            for (Element img : images) {
                String imageURL = img.attr("abs:src");
                if (imageURLs.size() >= limit) {
                    break;
                }
                imageURLs.add(imageURL);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return imageURLs;
    }

}
