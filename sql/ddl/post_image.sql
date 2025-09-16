DROP TABLE IF EXISTS post_image;
CREATE TABLE post_image (
    link VARCHAR(300) PRIMARY KEY,
    blog_domain_name VARCHAR(100) NOT NULL,
    image_url VARCHAR(200) NOT NULL,
    captured_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_image_link ON post_image (link);