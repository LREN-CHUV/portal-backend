#!/bin/sh

OPTS="-template /opt/portal/config/application.tmpl:/opt/portal/config/application.yml"
if [ ! -z "$PORTAL_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$PORTAL_DB_SERVER -timeout 60s"
fi
if [ ! -z "$META_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$META_DB_SERVER -timeout 60s"
fi
if [ ! -z "$FEATURES_DB_SERVER" ]; then
  OPTS="$OPTS -wait tcp://$FEATURES_DB_SERVER -timeout 60s"
fi
if [ ! -z "$HTTP_PROXY_HOST" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Dhttp.proxyHost=$HTTP_PROXY_HOST"
fi
if [ ! -z "$HTTP_PROXY_PORT" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Dhttp.proxyPort=$HTTP_PROXY_PORT"
fi
if [ ! -z "$HTTPS_PROXY_HOST" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Dhttps.proxyHost=$HTTPS_PROXY_HOST"
fi
if [ ! -z "$HTTPS_PROXY_PORT" ]; then
  JAVA_OPTIONS="$JAVA_OPTIONS -Dhttps.proxyPort=$HTTPS_PROXY_PORT"
fi
JAVA_OPTIONS="$JAVA_OPTIONS -Daeron.term.buffer.length=100m"

export SPRING_CONFIG_LOCATION=file:/opt/portal/config/application.yml

cd /opt/portal

dockerize $OPTS java ${JAVA_OPTIONS} -jar /usr/share/jars/portal-backend.jar
