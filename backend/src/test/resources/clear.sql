DROP TABLE IF EXISTS thumbnail_snippet;
DROP TABLE IF EXISTS snippet;
DROP TABLE IF EXISTS template_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS template;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS member;

create table member
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    username    VARCHAR(255) NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    primary key (id)
);

CREATE TABLE category
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    member_id   BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    is_default  TINYINT(1)   NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT name_with_member UNIQUE (member_id, name)
);

CREATE TABLE template
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    member_id   BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT       NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

create table tag
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
);

create table template_tag
(
    template_id BIGINT      NOT NULL,
    tag_id      BIGINT      NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    PRIMARY KEY (template_id, tag_id),
    FOREIGN KEY (template_id) REFERENCES template (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);

CREATE TABLE snippet
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    template_id BIGINT       NOT NULL,
    filename    VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    ordinal     INTEGER      NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (template_id) REFERENCES template (id)
);

CREATE TABLE thumbnail_snippet
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    template_id BIGINT      NOT NULL,
    snippet_id  BIGINT      NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (template_id) REFERENCES template (id),
    FOREIGN KEY (snippet_id) REFERENCES snippet (id)
);

INSERT INTO member (email, password, username, created_at, modified_at)
VALUES ('test1@email.com', 'password1234', 'username1', '2024-08-06', '2024-08-06');
INSERT INTO member (email, password, username, created_at, modified_at)
VALUES ('test2@email.com', 'password1234', 'username2', '2024-08-06', '2024-08-06');

INSERT INTO category (member_id, name, is_default, created_at, modified_at)
VALUES (1, '카테고리 없음', true, '2024-08-06', '2024-08-06');
INSERT INTO category (member_id, name, is_default, created_at, modified_at)
VALUES (2, '카테고리 없음', true, '2024-08-06', '2024-08-06');