#!/bin/bash -e
if groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER_COMPOSE="docker-compose"
else
  DOCKER_COMPOSE="sudo docker-compose"
fi

[ -e .m2 ] || ln -s .m2 ~/.m2

$DOCKER_COMPOSE --file=docker-compose.init.yml --project-name=portal-backend-init up

echo "Need to set the current user as owner of the files generated in target directory..."
sudo chown -R $USER:$USER ./target

echo "Done"
