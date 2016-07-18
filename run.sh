#!/usr/bin/env bash

docker-compose build
docker-compose up -d db

echo 'Waiting for database to be ready...'
until [ $(docker-compose exec portal-db psql -U postgres -c "\q" | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done

docker-compose up -d backend
