ALTER TABLE category ADD COLUMN ordinal INTEGER NOT NULL;
ALTER TABLE category ADD CONSTRAINT ordinal_with_member UNIQUE (member_id, ordinal);
