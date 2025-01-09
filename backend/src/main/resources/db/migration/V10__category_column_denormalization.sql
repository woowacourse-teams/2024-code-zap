ALTER TABLE category ADD COLUMN template_count BIGINT DEFAULT 0;

UPDATE category c
SET template_count = (
    SELECT COUNT(1)
    FROM template t
    WHERE t.category_id = c.id
);
