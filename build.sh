#!/usr/bin/env bash

./halt.sh
docker-compose -f ./docker-compose_build.yml build
docker-compose -f ./docker-compose_build.yml up -d
docker-compose stop db