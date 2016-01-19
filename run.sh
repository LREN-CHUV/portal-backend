#!/bin/bash -e

if groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER="docker"
else
  DOCKER="sudo docker"
fi

$DOCKER run -p 8080:8080 -d portal-backend
