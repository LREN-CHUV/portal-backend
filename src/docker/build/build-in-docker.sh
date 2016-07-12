#!/usr/bin/env bash

rm -r target/
mvn package

echo 'Waiting for postgres to be ready...'
until psql -h "db" -U "postgres" -c '\l'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

if [ $(psql -h "db" -U "postgres" -c "\dt" | grep schema_version | wc -l) == 0 ]; then
  mvn flyway:baseline
fi

mvn flyway:migrate

# Uncomment to generate a PDF API documentation
# mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc
