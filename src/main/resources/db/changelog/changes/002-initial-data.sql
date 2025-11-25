--liquibase formatted sql

--changeset alexandr:002-initial-data
INSERT INTO roles (name)
VALUES
    ('USER'),
    ('ADMIN')
ON CONFLICT (name) DO NOTHING;