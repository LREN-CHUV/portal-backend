#!/usr/bin/env bash

docker-compose -f ./docker-compose_build.yml build
docker-compose -f ./docker-compose_build.yml up
