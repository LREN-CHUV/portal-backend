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

docker run --name portal-db-test -p 5432:5432 -e POSTGRES_USER=postgres -d postgres:9.5.3

echo "Waiting for DB to start..."
until [ $(docker exec portal-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done

GATEWAY_IP=$(docker inspect portal-db-test | grep \"Gateway\":\ \" | sed 's/.*Gateway\":\ \"\([^-]*\)\",/\1/' | head -n 1)

docker run --name portal-backend-test -p 8080:8080 \
-e "AUTHENTICATION=0" \
-e "DB_URL=jdbc:postgresql://$GATEWAY_IP:5433/postgres" \
-e "META_DB_URL=jdbc:postgresql://$GATEWAY_IP:5432/postgres" \
-d hbpmip/portal-backend

echo "Waiting for backend to start..."
until [ $(docker logs portal-backend-test | grep "Started MIPApplication" | wc -l) -eq 1 ]; do
    printf '.'
    sleep 1
done
