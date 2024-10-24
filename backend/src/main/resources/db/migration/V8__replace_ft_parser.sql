ALTER TABLE template DROP INDEX idx_template_fulltext;
ALTER TABLE template ADD FULLTEXT INDEX idx_template_fulltext (title, description) WITH PARSER ngram;
