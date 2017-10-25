#!/usr/bin/env bash

set -e

get_script_dir () {
     SOURCE="${BASH_SOURCE[0]}"

     while [ -h "$SOURCE" ]; do
          DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
          SOURCE="$( readlink "$SOURCE" )"
          [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
     done
     cd -P "$( dirname "$SOURCE" )"
     pwd
}

cd "$(get_script_dir)"

if pgrep -lf sshuttle > /dev/null ; then
  echo "sshuttle detected. Please close this program as it messes with networking and prevents builds inside Docker to work"
  exit 1
fi

if [[ $NO_SUDO || -n "$CIRCLECI" ]]; then
  DOCKER="docker"
  DOCKER_COMPOSE="docker-compose"
elif groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER="docker"
  DOCKER_COMPOSE="docker-compose"
else
  DOCKER="sudo docker"
  DOCKER_COMPOSE="sudo docker-compose"
fi

function _cleanup() {
  local error_code="$?"
  echo "Stopping the containers..."
  $DOCKER_COMPOSE stop | true
  $DOCKER_COMPOSE down | true
  $DOCKER_COMPOSE rm -f > /dev/null 2> /dev/null | true
  exit $error_code
}
trap _cleanup EXIT INT TERM

if [ $($DOCKER network ls | grep -c 'portal-bridge') -lt 1 ]; then
  echo "Create portal-bridge network..."
  $DOCKER network create portal-bridge
else
  echo "Found portal-bridge network !"
fi

$DOCKER_COMPOSE up -d --remove-orphans db
$DOCKER_COMPOSE run wait_dbs
$DOCKER_COMPOSE run create_dbs

echo "Migrate metadata database..."
$DOCKER_COMPOSE run meta_db_setup

echo "Migrate features database..."
$DOCKER_COMPOSE run sample_db_setup

echo
echo "Start Portal Backend"
$DOCKER_COMPOSE up -d portal_backend

echo
echo "Wait for Portal Backend to start..."
$DOCKER_COMPOSE run wait_portal_backend

echo
echo "Test idempotence"
#$DOCKER_COMPOSE run data_db_setup
#$DOCKER_COMPOSE run data_db_check

# Cleanup
#_cleanup
