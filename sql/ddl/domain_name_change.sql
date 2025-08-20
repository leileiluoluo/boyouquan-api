DROP TABLE IF EXISTS domain_name_change;
CREATE TABLE domain_name_change (
    id INT AUTO_INCREMENT PRIMARY KEY,
    old_domain_name VARCHAR(100) NOT NULL,
    new_domain_name VARCHAR(100) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_domain_name_change ON domain_name_change (old_domain_name);