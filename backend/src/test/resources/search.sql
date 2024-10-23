-- 모든 테이블의 데이터 삭제
DELETE FROM template_tag;
DELETE FROM thumbnail;
DELETE FROM source_code;
DELETE FROM template;
DELETE FROM category;
DELETE FROM tag;
DELETE FROM member;

-- 이제 새로운 데이터 삽입
-- Member 삽입
INSERT INTO member (id, created_at, modified_at, name, password, salt)
VALUES (1, '2024-09-27 08:43:08.752936', '2024-09-27 08:43:08.752936', '몰리', 'password1234', 'salt1');

INSERT INTO member (id, created_at, modified_at, name, password, salt)
VALUES (2, '2024-09-27 08:43:08.757482', '2024-09-27 08:43:08.757482', '몰리2', 'password1234', 'salt2');

-- Category 삽입
INSERT INTO category (id, created_at, is_default, member_id, modified_at, name)
VALUES (1, '2024-09-27 08:43:08.760511', false, 1, '2024-09-27 08:43:08.760511', 'Category 1');

INSERT INTO category (id, created_at, is_default, member_id, modified_at, name)
VALUES (2, '2024-09-27 08:43:08.763888', false, 1, '2024-09-27 08:43:08.763888', 'Category 2');

-- Tag 삽입
INSERT INTO tag (id, created_at, modified_at, name)
VALUES (1, '2024-09-27 08:43:08.767154', '2024-09-27 08:43:08.767154', 'Tag 1');

INSERT INTO tag (id, created_at, modified_at, name)
VALUES (2, '2024-09-27 08:43:08.769440', '2024-09-27 08:43:08.769440', 'Tag 2');

-- Template 삽입
INSERT INTO template (id, category_id, created_at, description, member_id, modified_at, title, visibility)
VALUES (1, 1, '2024-09-27 08:43:08.773369', 'Description 1', 1, '2024-09-27 08:43:08.773369', 'Template 1', 'PUBLIC');

INSERT INTO template (id, category_id, created_at, description, member_id, modified_at, title, visibility)
VALUES (2, 2, '2024-09-27 08:43:08.787995', 'Description 1', 1, '2024-09-27 08:43:08.787995', 'Template 1', 'PUBLIC');

INSERT INTO template (id, category_id, created_at, description, member_id, modified_at, title, visibility)
VALUES (3, 1, '2024-09-27 08:43:08.790778', 'Description 1', 2, '2024-09-27 08:43:08.790778', 'Template 1', 'PUBLIC');

INSERT INTO template (id, category_id, created_at, description, member_id, modified_at, title, visibility)
VALUES (4, 2, '2024-09-27 08:43:08.790780', 'Description 1', 2, '2024-09-27 08:43:08.790780', 'Template 1', 'PRIVATE');

-- Source Code 삽입
INSERT INTO source_code (id, content, created_at, filename, modified_at, ordinal, template_id)
VALUES (1, 'content1', '2024-09-27 08:43:08.793152', 'filename1', '2024-09-27 08:43:08.793152', 1, 1);

INSERT INTO source_code (id, content, created_at, filename, modified_at, ordinal, template_id)
VALUES (2, 'content2', '2024-09-27 08:43:08.797349', 'filename2', '2024-09-27 08:43:08.797349', 2, 2);

INSERT INTO source_code (id, content, created_at, filename, modified_at, ordinal, template_id)
VALUES (3, 'content1', '2024-09-27 08:43:08.810641', 'filename1', '2024-09-27 08:43:08.810641', 1, 3);

INSERT INTO source_code (id, content, created_at, filename, modified_at, ordinal, template_id)
VALUES (4, 'content1', '2024-09-27 08:43:08.790783', 'filename1', '2024-09-27 08:43:08.790783', 1, 4);

-- Thumbnail 삽입
INSERT INTO thumbnail (id, created_at, modified_at, source_code_id, template_id)
VALUES (1, '2024-09-27 08:43:08.812840', '2024-09-27 08:43:08.812840', 1, 1);

INSERT INTO thumbnail (id, created_at, modified_at, source_code_id, template_id)
VALUES (2, '2024-09-27 08:43:08.815866', '2024-09-27 08:43:08.815866', 2, 2);

INSERT INTO thumbnail (id, created_at, modified_at, source_code_id, template_id)
VALUES (3, '2024-09-27 08:43:08.817850', '2024-09-27 08:43:08.817850', 3, 3);

INSERT INTO thumbnail (id, created_at, modified_at, source_code_id, template_id)
VALUES (4, '2024-09-27 08:43:08.817850', '2024-09-27 08:43:08.817850', 4, 4);

-- Template_Tag 삽입
INSERT INTO template_tag (created_at, modified_at, tag_id, template_id)
VALUES ('2024-09-27 08:43:08.835179', '2024-09-27 08:43:08.835179', 1, 1);

INSERT INTO template_tag (created_at, modified_at, tag_id, template_id)
VALUES ('2024-09-27 08:43:08.843467', '2024-09-27 08:43:08.843467', 2, 1);

INSERT INTO template_tag (created_at, modified_at, tag_id, template_id)
VALUES ('2024-09-27 08:43:08.851712', '2024-09-27 08:43:08.851712', 1, 2);

INSERT INTO template_tag (created_at, modified_at, tag_id, template_id)
VALUES ('2024-09-27 08:43:08.860091', '2024-09-27 08:43:08.860091', 2, 2);

INSERT INTO template_tag (created_at, modified_at, tag_id, template_id)
VALUES ('2024-09-27 08:43:08.867619', '2024-09-27 08:43:08.867619', 2, 3);

-- 템플릿 테이블에 전문 검색 인덱스가 존재하는지 조회
SELECT COUNT(1) INTO @indexExists FROM information_schema.statistics
WHERE table_name = 'template'
  AND index_name = 'idx_template_fulltext';

-- 없다면 전문 검색 인덱스 생성
SET @createIndex = IF(@indexExists = 0, 'CREATE FULLTEXT INDEX idx_template_fulltext ON template (title, description);', 'SELECT 1');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 소스 코드 테이블에 전문 검색 인덱스가 존재하는지 조회
SELECT COUNT(1) INTO @indexExists FROM information_schema.statistics
WHERE table_name = 'source_code'
  AND index_name = 'idx_source_code_fulltext';

-- 없다면 전문 검색 인덱스 생성
SET @createIndex = IF(@indexExists = 0, 'CREATE FULLTEXT INDEX idx_source_code_fulltext ON source_code (content, filename);', 'SELECT 1');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
