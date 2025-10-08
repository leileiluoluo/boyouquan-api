DROP TABLE IF EXISTS likes;
CREATE TABLE likes (
    year_month_str VARCHAR(10) NOT NULL,
    type ENUM('MOMENTS') DEFAULT NULL,
    entity_id INT NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    PRIMARY KEY (year_month_str, type, entity_id)
);

CREATE INDEX idx_likes_type_entity_id ON likes (type, entity_id);
