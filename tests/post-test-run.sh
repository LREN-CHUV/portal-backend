#!/usr/bin/env bash


# Kill old containers

docker kill backend-test
docker kill portal-db-test
docker kill science-db-test


# Remove old containers

docker rm -f backend-test
docker rm -f portal-db-test
docker rm -f science-db-test
