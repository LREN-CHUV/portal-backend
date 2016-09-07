#!/bin/sh

OPTS="-template /config/application.tmpl:/config/application.yml"
if [ ! -z "$DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$DB_SERVER -timeout 60s"
fi
if [ ! -z "$META_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$META_DB_SERVER -timeout 60s"
fi
if [ ! -z "$ADNI_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$ADNI_DB_SERVER -timeout 60s"
fi
dockerize $OPTS java -jar backend.jar
