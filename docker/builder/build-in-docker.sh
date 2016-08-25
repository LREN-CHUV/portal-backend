#!/usr/bin/env bash

echo "Removing old target folder..."
rm -rf target/

echo "Packaging..."
mvn package
