DROP TABLE IF EXISTS subscription;
CREATE TABLE subscription (
    email VARCHAR(100) NOT NULL,
    type ENUM('MONTHLY_SELECTED') NOT NULL,
    subscribed_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    unsubscribed_at TIMESTAMP NULL DEFAULT NULL,
    unsubscribed BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_subscription_email_status ON subscription (email, type);