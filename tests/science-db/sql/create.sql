SET datestyle to 'European';

CREATE SCHEMA IF NOT EXISTS science;

CREATE TABLE science.ADNI_MERGE
(
  tv1 char(256),
  tv2 numeric,
  tv3 numeric,

  CONSTRAINT pk_adni_merge PRIMARY KEY (tv2)
)
WITH (
  OIDS=FALSE
);

COPY science.ADNI_MERGE FROM '/docker-entrypoint-initdb.d/values.csv' CSV HEADER;
