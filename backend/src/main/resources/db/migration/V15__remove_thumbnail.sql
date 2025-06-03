ALTER TABLE template ADD COLUMN thumbnail_ordinal BIGINT NOT NULL DEFAULT 0;
UPDATE template t
SET thumbnail_ordinal = (
    SELECT id FROM thumbnail th WHERE th.template_id = t.id
);

DROP TABLE IF EXISTS thumbnail;

