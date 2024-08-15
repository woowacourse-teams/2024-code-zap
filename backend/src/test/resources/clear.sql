DELETE FROM thumbnail_snippet;
DELETE FROM snippet;
DELETE FROM template_tag;
DELETE FROM tag;
DELETE FROM template;
DELETE FROM category;
DELETE FROM member;

ALTER TABLE thumbnail_snippet ALTER COLUMN id RESTART WITH 1;
ALTER TABLE snippet ALTER COLUMN id RESTART WITH 1;
ALTER TABLE template_tag ALTER COLUMN template_id RESTART WITH 1;
ALTER TABLE tag ALTER COLUMN id RESTART WITH 1;
ALTER TABLE template ALTER COLUMN id RESTART WITH 1;
ALTER TABLE category ALTER COLUMN id RESTART WITH 1;
ALTER TABLE member ALTER COLUMN id RESTART WITH 1;

INSERT INTO member (username, password, created_at, modified_at)
VALUES ('username1', 'password1234', '2024-08-06 00:00:00', '2024-08-06 00:00:00');
INSERT INTO member (username, password, created_at, modified_at)
VALUES ('username2', 'password1234', '2024-08-06 00:00:00', '2024-08-06 00:00:00');

INSERT INTO category (member_id, name, is_default, created_at, modified_at)
VALUES (1, '카테고리 없음', TRUE, '2024-08-06 00:00:00', '2024-08-06 00:00:00');
INSERT INTO category (member_id, name, is_default, created_at, modified_at)
VALUES (2, '카테고리 없음', TRUE, '2024-08-06 00:00:00', '2024-08-06 00:00:00');

