#!/usr/bin/env bash

rm -r target/
mvn package

echo 'Waiting for postgres to be ready...'
until psql -h "db" -U "postgres" -c '\l'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

mvn flyway:baseline flyway:migrate

# Uncomment to generate a PDF API documentation
# mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc
