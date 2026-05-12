DROP TABLE IF EXISTS post_detail;
CREATE TABLE post_detail (
    link VARCHAR(300) PRIMARY KEY,
    blog_domain_name VARCHAR(100) NOT NULL,
    content VARCHAR(5000),
    content_refined VARCHAR(5000),
    updated_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_detail_link ON post_detail (link);
CREATE INDEX idx_post_detail_domain_name ON post_detail (blog_domain_name);
