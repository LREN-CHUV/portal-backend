#!/usr/bin/env bash

if [ $(docker ps | grep portal-db | wc -l) -gt 0 ]; then
  docker kill portal-db
fi
if [ $(docker ps -a | grep portal-db | wc -l) -gt 0 ]; then
  docker rm -f portal-db
fi

if [ $(docker ps | grep portal-backend | wc -l) -gt 0 ]; then
  docker kill portal-backend
fi
if [ $(docker ps -a | grep portal-backend | wc -l) -gt 0 ]; then
  docker rm -f portal-backend
fi

docker run --name portal-db -p 5432:5432 -e POSTGRES_USER=postgres -d postgres

echo 'Wating for DB to start...'
until [ $(docker exec portal-db psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done

docker run --name portal-backend -p 8080:8080 \
-e "AUTHENTICATION=0" \
-d hbpmip/portal-backend

echo 'Wating for backend to start...'
until [ $(docker logs portal-backend | grep "Started MIPApplication" | wc -l) -eq 1 ]; do
    printf '.'
    sleep 1
done
