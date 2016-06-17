#!/bin/bash -e

if groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER_COMPOSE="docker-compose"
else
  DOCKER_COMPOSE="sudo docker-compose"
fi

# TODO: Cannot clean otherwise the build fails because of Hibernate schema generation
mvn package -P dev -Dmaven.test.skip=TRUE
cp target/mip.jar src/docker/build/

$DOCKER_COMPOSE build
