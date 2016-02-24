#!/bin/bash -e

if groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER_COMPOSE="docker-compose"
else
  DOCKER_COMPOSE="sudo docker-compose"
fi

cp target/mip.jar src/docker/build/

$DOCKER_COMPOSE build
