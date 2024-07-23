DROP TABLE IF EXISTS thumbnail_snippet;
DROP TABLE IF EXISTS snippet;
DROP TABLE IF EXISTS template;

CREATE TABLE template
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
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
    id          BIGINT NOT NULL AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    snippet_id  BIGINT NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (template_id) REFERENCES template (id),
    FOREIGN KEY (snippet_id) REFERENCES snippet (id)
);
