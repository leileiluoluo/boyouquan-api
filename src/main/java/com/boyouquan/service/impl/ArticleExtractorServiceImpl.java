package com.boyouquan.service.impl;

import com.boyouquan.service.ArticleExtractorService;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ArticleExtractorServiceImpl implements ArticleExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleExtractorServiceImpl.class);
    private static final int TIME_OUT = 10000;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    @Override
    public String getContent(String url) {
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

            return result.toString().trim();

        } catch (IOException e) {
            logger.error("提取文章失败: {}", e.getMessage(), e);
            return "提取失败：" + e.getMessage();
        }
    }

}