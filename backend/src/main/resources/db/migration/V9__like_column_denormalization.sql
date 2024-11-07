ALTER TABLE template ADD COLUMN likes_count BIGINT DEFAULT 0;

UPDATE template t
SET likes_count = (
    SELECT COUNT(1)
    FROM likes l
    WHERE l.template_id = t.id
);
