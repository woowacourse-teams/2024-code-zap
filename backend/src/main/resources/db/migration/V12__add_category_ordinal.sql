START TRANSACTION;

ALTER TABLE category ADD COLUMN ordinal INTEGER NOT NULL;

CREATE TEMPORARY TABLE temp_category AS
SELECT
    id,
    ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY id) - 1 AS new_ordinal
FROM category;

UPDATE category c
    JOIN temp_category t ON c.id = t.id
    SET c.ordinal = t.new_ordinal;

DROP TEMPORARY TABLE temp_category;

ALTER TABLE category ADD CONSTRAINT ordinal_with_member UNIQUE (member_id, ordinal);

COMMIT;
