DROP TABLE IF EXISTS access;
CREATE TABLE access (
    link VARCHAR(200) NOT NULL,
    blog_domain_name VARCHAR(100) NOT NULL,
    ip VARCHAR(100) NOT NULL,
    accessed_at TIMESTAMP NOT NULL
);