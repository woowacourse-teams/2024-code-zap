ALTER TABLE template ADD FULLTEXT INDEX idx_template_fulltext (title, description);
ALTER TABLE source_code ADD FULLTEXT INDEX idx_source_code_fulltext (content, filename);
