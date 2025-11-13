DROP TABLE IF EXISTS post;
CREATE TABLE post (
    link VARCHAR(300) PRIMARY KEY,
    blog_domain_name VARCHAR(100) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    published_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    draft BOOLEAN DEFAULT FALSE,
    recommended BOOLEAN DEFAULT FALSE,
    pinned BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_domain_name ON post (blog_domain_name);
CREATE INDEX idx_post_recommended ON post (recommended);
CREATE INDEX idx_post_pinned ON post (pinned);
CREATE INDEX idx_post_domain_published_status ON post(blog_domain_name, published_at DESC, draft, deleted);
CREATE INDEX idx_post_latest_posts ON post(blog_domain_name, draft, deleted, published_at DESC);