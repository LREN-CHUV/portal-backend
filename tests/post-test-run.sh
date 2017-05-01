#!/usr/bin/env bash


# Kill old containers

if [ $(docker ps | grep backend-test | wc -l) -gt 0 ]; then
  docker kill backend-test
fi
if [ $(docker ps | grep portal-db-test | wc -l) -gt 0 ]; then
  docker kill portal-db-test
fi
if [ $(docker ps | grep features-db-test | wc -l) -gt 0 ]; then
  docker kill features-db-test
fi
if [ $(docker ps | grep meta-db-test | wc -l) -gt 0 ]; then
  docker kill meta-db-test
fi


# Remove old containers

if [ $(docker ps -a | grep backend-test | wc -l) -gt 0 ]; then
  docker rm -f backend-test
fi
if [ $(docker ps -a | grep portal-db-test | wc -l) -gt 0 ]; then
  docker rm -f portal-db-test
fi
if [ $(docker ps -a | grep features-db-test | wc -l) -gt 0 ]; then
  docker rm -f features-db-test
fi
if [ $(docker ps -a | grep meta-db-test | wc -l) -gt 0 ]; then
  docker rm -f meta-db-test
fi
