INSERT INTO member (id, name, password, salt, created_at, modified_at)
VALUES (1, 'coco', 'password1234', 'salt1', '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445');

INSERT INTO category (id, name, is_default, member_id, ordinal, created_at, modified_at)
VALUES (1, '카테고리 없음', true, 1, 1, '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445');

-- Template 데이터 삽입
INSERT INTO template (id, title, description, likes_count, visibility, category_id, member_id, created_at, modified_at)
VALUES (1, '안녕', 'Description 1', 0, 'PUBLIC', 1, 1, '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445'),
       (2, '안녕2', 'Description 2', 0, 'PUBLIC', 1, 1, '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445');

-- Tag 데이터 삽입
INSERT INTO tag (id, name, created_at, modified_at)
VALUES (1, 'lastTag1', '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445'),
       (2, 'lastTag2', '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445');

-- TemplateTag 데이터 삽입
INSERT INTO template_tag (template_id, tag_id, created_at, modified_at)
VALUES (1, 1, '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445'),
       (1, 2, '2023-12-17 02:05:30.165445', '2023-12-17 02:05:30.165445');
