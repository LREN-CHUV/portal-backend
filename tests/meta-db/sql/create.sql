CREATE DATABASE meta;
\c meta

SET datestyle to 'European';

CREATE TABLE IF NOT EXISTS meta_variables (
  ID serial NOT NULL PRIMARY KEY,
  source varchar(256) UNIQUE NOT NULL,
  hierarchy json NOT NULL
);
INSERT INTO meta_variables (source, hierarchy) VALUES (
  'adni',
  '
  {
    "code": "root",
    "groups": [{
        "code": "tg1",
        "label": "Test Group 1",
        "groups": [{
            "code": "tg3",
            "label": "Test Group 3",
            "variables": [{
                "code": "tv1",
                "label": "Test Variable 1",
                "type": "text"
            }]
        }]
    }, {
        "code": "tg2",
        "label": "Test Group 2",
        "groups": [{
            "code": "tg4",
            "label": "Test Group 4",
            "variables": [{
                "code": "tv2",
                "label": "Test Variable 2",
                "type": "integer"
            }, {
                "code": "tv3",
                "label": "Test Variable 3",
                "type": "real"
            }]
        }]
    }]
  }
  '
);
