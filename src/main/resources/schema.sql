CREATE TABLE IF NOT EXISTS TRANSACTION
(
    ID                   BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    USER_ID              BIGINT                            NOT NULL,
    ORIGIN_CURRENCY      VARCHAR(3)                        NOT NULL,
    ORIGIN_VALUE         DOUBLE                            NOT NULL,
    DESTINATION_CURRENCY VARCHAR(3)                        NOT NULL,
    DESTINATION_VALUE    DOUBLE                            NOT NULL,
    CONVERSION_RATE      DOUBLE                            NOT NULL,
    TRANSACTION_DATE     DATETIME                          NOT NULL
);