DROP TABLE IF EXISTS moment;
CREATE TABLE moment (
    blog_domain_name VARCHAR(100),
    description VARCHAR(300) NOT NULL,
    image_url VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    updated_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_moment_blog_domain_name ON moment (blog_domain_name);