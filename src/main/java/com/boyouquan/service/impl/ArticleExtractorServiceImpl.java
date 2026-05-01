package com.boyouquan.service.impl;

import com.boyouquan.service.ArticleExtractorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ArticleExtractorServiceImpl implements ArticleExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleExtractorServiceImpl.class);

    private static final int TIME_OUT = 10000;

    public String getContent(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(TIME_OUT)
                    .get();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "未提取到正文";
        }

        cleanUselessTags(doc);
        Element contentElement = findMainContent(doc.body());

        if (contentElement == null) return "未提取到正文";

        // ====================== 修复：保留段落换行 ======================
        StringBuilder sb = new StringBuilder();
        for (Element p : contentElement.select("p")) {
            String text = p.text().trim();
            if (!text.isEmpty()) {
                sb.append(text).append("\n\n"); // 段落之间空两行
            }
        }
        return sb.toString().trim();
    }

    private static void cleanUselessTags(Document doc) {
        String[] uselessTags = {
                "script", "style", "noscript", "iframe", "header", "footer",
                "nav", "aside", "ad", "banner", "comment", "copyright", "sidebar"
        };
        for (String tag : uselessTags) {
            Elements elements = doc.select(tag);
            if (!elements.isEmpty()) elements.remove();
        }

        doc.select("div").forEach(div -> {
            String clazz = div.className().toLowerCase();
            String id = div.id().toLowerCase();
            if (clazz.contains("ad") || clazz.contains("nav") || clazz.contains("side") ||
                    id.contains("ad") || id.contains("nav") || id.contains("footer")) {
                div.remove();
            }
        });
    }

    private static Element findMainContent(Element body) {
        Elements pList = body.select("p");
        Element bestElem = body;
        int maxScore = 0;

        for (Element p : pList) {
            Element parent = p.parent();
            int score = calculateScore(parent);
            if (score > maxScore) {
                maxScore = score;
                bestElem = parent;
            }
        }
        return bestElem;
    }

    private static int calculateScore(Element e) {
        String text = e.text();
        int textLen = text.length();
        int pCount = e.select("p").size();
        if (textLen < 50) return 0;
        return textLen + pCount * 30;
    }

}
