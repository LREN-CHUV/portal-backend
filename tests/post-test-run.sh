#!/usr/bin/env bash

docker kill portal-db-test
docker rm -f portal-db-test

docker kill portal-backend-test
docker rm -f portal-backend-test
