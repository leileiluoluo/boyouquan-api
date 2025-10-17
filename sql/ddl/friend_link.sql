DROP TABLE IF EXISTS friend_link;
CREATE TABLE friend_link (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_blog_domain_name VARCHAR(100) NOT NULL,
    target_blog_domain_name VARCHAR(100) NOT NULL,
    page_title VARCHAR(200) NOT NULL,
    page_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    UNIQUE KEY uni_friend_link (source_blog_domain_name, target_blog_domain_name, page_url)
);

CREATE INDEX idx_friend_link_src_domain ON friend_link (source_blog_domain_name);
CREATE INDEX idx_friend_link_tar_domain ON friend_link (target_blog_domain_name);
