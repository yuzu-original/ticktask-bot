-- liquibase formatted sql

-- changeset yuzu:1
CREATE TABLE notification_task (
    id SERIAL,
    chat_id BIGINT NOT NULL,
    text VARCHAR(255) NOT NULL,
    datetime TIMESTAMP NOT NULL
)