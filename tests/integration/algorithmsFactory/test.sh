#!/usr/bin/env bash

curl http://backend:8080/services/models -X POST -H "Content-Type: application/json" -d @model1.json
curl http://backend:8080/services/experiments -X POST -H "Content-Type: application/json" -d @expQuery1.json
