#!/bin/sh

OPTS="-template /config/application.tmpl:/config/application.yml"
if [ ! -z "$DB_SERVER" ]; then
  OPTS="$OPTS -wait $DB_SERVER -timeout 60s"
fi
dockerize $OPTS java -jar backend.jar
