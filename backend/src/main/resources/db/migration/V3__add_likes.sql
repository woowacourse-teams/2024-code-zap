-- Create tables
CREATE TABLE likes
(
    created_at  DATETIME(6) NOT NULL,
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    member_id   BIGINT      NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    template_id BIGINT      NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Add foreign key constraints
ALTER TABLE likes
    ADD CONSTRAINT FKa4vkf1skcfu5r6o5gfb5jf295 FOREIGN KEY (member_id) REFERENCES member (id);
ALTER TABLE likes
    ADD CONSTRAINT FKbrgthyu05fsswb9ysi2l79g7r FOREIGN KEY (template_id) REFERENCES template (id);
