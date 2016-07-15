#!/usr/bin/env bash

echo 'Removing old target folder...'
rm -r target/

echo 'Packaging...'
mvn package
