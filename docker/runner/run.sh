#!/bin/sh

OPTS="-template /config/application.tmpl:/config/application.yml"
if [ ! -z "$PORTAL_DB_URL" ]; then
  OPTS="$OPTS -wait tcp://$PORTAL_DB_URL -timeout 60s"
fi
if [ ! -z "$META_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$META_DB_SERVER -timeout 60s"
fi
if [ ! -z "$SCIENCE_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$SCIENCE_DB_SERVER -timeout 60s"
fi
dockerize $OPTS java -jar backend.jar