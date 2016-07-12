#!/usr/bin/env bash

echo 'Removing old target folder...'
rm -r target/

echo 'Packaging...'
mvn package

echo 'Waiting for postgres to be ready...'
until psql -h "db" -U "postgres" -c '\l'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

if [ $(psql -h "db" -U "postgres" -c "\dt" | grep schema_version | wc -l) == 0 ]; then
  echo 'Generating database baseline...'
  mvn flyway:baseline
fi

echo 'Migrating database...'
mvn flyway:migrate

# Uncomment to generate a PDF API documentation
# mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc
