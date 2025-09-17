DROP TABLE IF EXISTS post_image;
CREATE TABLE post_image (
    link VARCHAR(300) PRIMARY KEY,
    image_type VARCHAR(20) NOT NULL,
    image_size_kb INT NOT NULL,
    year_str VARCHAR(10) NOT NULL,
    month_str VARCHAR(10) NOT NULL,
    raw_image_url VARCHAR(200) NOT NULL,
    image_url VARCHAR(200) NOT NULL,
    captured_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    updated_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_image_link ON post_image (link);