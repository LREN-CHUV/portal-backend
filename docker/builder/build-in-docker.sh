#!/usr/bin/env bash

echo ">>> Building inside Docker"

echo "Removing old target folder..."
rm -rf target/

echo "Packaging..."
mvn package

echo ">>> Backend build is complete"
