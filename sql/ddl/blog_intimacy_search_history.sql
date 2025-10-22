DROP TABLE IF EXISTS blog_intimacy_search_history;
CREATE TABLE blog_intimacy_search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_blog_domain_name VARCHAR(100) NOT NULL,
    target_blog_domain_name VARCHAR(100) NOT NULL,
    path_length INT NOT NULL,
    ip_address VARCHAR(100) NOT NULL,
    searched_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00'
);