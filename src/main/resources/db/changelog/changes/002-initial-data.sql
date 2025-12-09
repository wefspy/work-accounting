-- liquibase formatted sql

-- changeset alexandr:002-initial-data
INSERT INTO roles (name)
VALUES
    ('USER'),
    ('ADMIN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO project_status (name, version)
VALUES
    ('UNDER_DISCUSSION', 0),
    ('VOTING', 0),
    ('ARCHIVED', 0),
    ('APPROVED', 0),
    ('IN_PROGRESS', 0)
ON CONFLICT (name) DO NOTHING;