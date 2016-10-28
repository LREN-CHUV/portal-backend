ALTER TABLE query ADD COLUMN sql_filter text;

ALTER TABLE query_filter DROP COLUMN filters_id;
DROP TABLE filter_values, filter;

ALTER TABLE query_filter ADD COLUMN code character varying(255) NOT NULL DEFAULT '';
ALTER TABLE ONLY query_filter
    ADD CONSTRAINT fk_1 FOREIGN KEY (query_id) REFERENCES query(id);