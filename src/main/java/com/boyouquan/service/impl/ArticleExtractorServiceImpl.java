package com.boyouquan.service.impl;

import com.boyouquan.service.ArticleExtractorService;
import com.boyouquan.service.PostService;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleExtractorServiceImpl implements ArticleExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleExtractorServiceImpl.class);
    private static final int TIME_OUT = 10000;
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; Boyouquanspider/1.0; +https://www.boyouquan.com/about#data-spider)";

    @Autowired
    private PostService postService;

    @Override
    public String getContent(String url) {
        boolean contentValid = true;

        try {
            // 1. 抓取页面
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIME_OUT)
                    .get();

            // 2. Readability 提取文章（只拿清洗后的 HTML，不拿纯文本）
            Readability4J readability4J = new Readability4J(url, doc.html());
            Article article = readability4J.parse();
            String contentHtml = article.getContent(); // 清洗后的正文 HTML（带<p>）

            if (contentHtml == null || contentHtml.isBlank()) {
                contentValid = false;
                return "未提取到正文";
            }

            // 3. 重新解析 HTML，按 <p> 分段，保留换行！！！
            Document contentDoc = Jsoup.parse(contentHtml);
            StringBuilder result = new StringBuilder();

            for (Element p : contentDoc.select("p")) {
                String text = p.text().trim();
                if (!text.isBlank()) {
                    result.append(text).append("\n\n"); // 段落之间空两行
                }
            }

            String finalContent = result.toString().trim();
            if (finalContent.length() < 30) {
                contentValid = false;
            }

            return finalContent;
        } catch (Exception e) {
            contentValid = false;
            logger.error("提取文章失败: {}", e.getMessage(), e);
            return "提取失败：" + e.getMessage();
        } finally {
            if (!contentValid) {
                postService.updateContentValid(url, false);
            }
        }
    }

}