SET datestyle to 'European';

CREATE TABLE IF NOT EXISTS FEATURES
(
  tv1 char(256),
  tv2 numeric,
  tv3 numeric,

  CONSTRAINT pk_features PRIMARY KEY (tv2)
)
WITH (
  OIDS=FALSE
);

COPY FEATURES FROM '/docker-entrypoint-initdb.d/values.csv' CSV HEADER;
