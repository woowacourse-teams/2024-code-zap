-- Create tables
CREATE TABLE category
(
    is_default  BIT          NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    member_id   BIGINT       NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE member
(
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE source_code
(
    ordinal     INTEGER      NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6) NOT NULL,
    template_id BIGINT       NOT NULL,
    content     TEXT         NOT NULL,
    filename    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE tag
(
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE template
(
    category_id BIGINT       NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    member_id   BIGINT       NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    description TEXT,
    title       VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE template_tag
(
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    tag_id      BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    PRIMARY KEY (tag_id, template_id)
) ENGINE=InnoDB;

CREATE TABLE thumbnail
(
    created_at     DATETIME(6) NOT NULL,
    id             BIGINT NOT NULL AUTO_INCREMENT,
    modified_at    DATETIME(6) NOT NULL,
    source_code_id BIGINT NOT NULL,
    template_id    BIGINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Add constraints
ALTER TABLE category
    ADD CONSTRAINT name_with_member UNIQUE (member_id, name);
ALTER TABLE member
    ADD CONSTRAINT UK9esvgikrmti1v7ci6v453imdc UNIQUE (name);
ALTER TABLE thumbnail
    ADD CONSTRAINT UKi6dbyoqre1oeei7309am7m65p UNIQUE (source_code_id);
ALTER TABLE thumbnail
    ADD CONSTRAINT UKqvae6g2xuj6an3wjyc7wau2eb UNIQUE (template_id);

-- Add foreign key constraints
ALTER TABLE category
    ADD CONSTRAINT FKd7qtd46ngp06lnc19g6wtoh8t FOREIGN KEY (member_id) REFERENCES member (id);
ALTER TABLE source_code
    ADD CONSTRAINT FKjax3t3l28tqs65iqfd3ty5dxw FOREIGN KEY (template_id) REFERENCES template (id);
ALTER TABLE template
    ADD CONSTRAINT FKfke34lch7qf4xixqnyj3h12m7 FOREIGN KEY (category_id) REFERENCES category (id);
ALTER TABLE template
    ADD CONSTRAINT FKodvst19gfi7in0ox3dfocuejx FOREIGN KEY (member_id) REFERENCES member (id);
ALTER TABLE template_tag
    ADD CONSTRAINT FK460d2kdmrpffumqaahvh414mv FOREIGN KEY (tag_id) REFERENCES tag (id);
ALTER TABLE template_tag
    ADD CONSTRAINT FKnq3tyggcae26yafql1l0rc91l FOREIGN KEY (template_id) REFERENCES template (id);
ALTER TABLE thumbnail
    ADD CONSTRAINT FKssc68bv1tyrlqpi3fet97rbi FOREIGN KEY (source_code_id) REFERENCES source_code (id);
ALTER TABLE thumbnail
    ADD CONSTRAINT FKgxg8st4s8hfkmbb3mbm34ww49 FOREIGN KEY (template_id) REFERENCES template (id);
