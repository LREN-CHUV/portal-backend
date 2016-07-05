#!/usr/bin/env bash

rm -r target/
mvn package -DskipTests -P dev
