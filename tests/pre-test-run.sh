#!/usr/bin/env bash

if [ $(docker ps | grep portal-db-test | wc -l) -gt 0 ]; then
  docker kill portal-db-test
fi
if [ $(docker ps -a | grep portal-db-test | wc -l) -gt 0 ]; then
  docker rm -f portal-db-test
fi

if [ $(docker ps | grep portal-backend-test | wc -l) -gt 0 ]; then
  docker kill portal-backend-test
fi
if [ $(docker ps -a | grep portal-backend-test | wc -l) -gt 0 ]; then
  docker rm -f portal-backend-test
fi

docker run --name portal-db-test -e POSTGRES_USER=postgres -d postgres
docker run --name portal-backend-test -p 8000:8000 \
-e "DB_URL=jdbc:postgresql://172.22.0.1:5432/postgres" \
-e "DB_USER=postgres" \
-e "CONTEXT_PATH=/services" \
-e "PORT=8080" \
-d hbpmip/portal-backend
