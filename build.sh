#!/usr/bin/env bash

./halt.sh
docker-compose -f ./docker-compose_build.yml build
docker-compose -f ./docker-compose_build.yml up -d

until [ -e ./target/backend-services-DOCKER_BUILD.jar ]; do
  echo 'Waiting...'
  sleep 1
done
