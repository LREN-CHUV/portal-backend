#!/bin/bash -e
if groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER_COMPOSE="docker-compose"
else
  DOCKER_COMPOSE="sudo docker-compose"
fi

# Create a symlink to the local Maven repository
[ -L .m2 ] || ln -s -t . ~/.m2

$DOCKER_COMPOSE --file=docker-compose.init.yml --project-name=portal-backend-init up

echo "Need to set the current user as owner of the files generated in target directory..."
sudo chown -R $USER:$USER ./target

echo "Done"
