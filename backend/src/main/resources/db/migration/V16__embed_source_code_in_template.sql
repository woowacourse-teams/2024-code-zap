CREATE TABLE source_code_temp (
                                  template_id BIGINT NOT NULL,
                                  ordinal INT NOT NULL,
                                  filename VARCHAR(255) NOT NULL,
                                  content TEXT NOT NULL
);

INSERT INTO source_code_temp (template_id, ordinal, filename, content)
SELECT template_id, ordinal, filename, content
FROM source_code;

DROP TABLE source_code;

ALTER TABLE source_code_temp RENAME TO source_code;
