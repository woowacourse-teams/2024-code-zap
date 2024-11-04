ALTER TABLE template ADD COLUMN likes_count BIGINT;

UPDATE template t
SET likes_count = (
    SELECT COUNT(1)
    FROM likes l
    WHERE l.template_id = t.id
);
