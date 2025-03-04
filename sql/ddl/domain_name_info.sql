DROP TABLE IF EXISTS domain_name_info;
CREATE TABLE domain_name_info (
    blog_domain_name VARCHAR(100) PRIMARY KEY,
    real_domain_name VARCHAR(200) NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    detected_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    refreshed_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    confirmed BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_domain_name_info_domain_name ON domain_name_info (blog_domain_name);