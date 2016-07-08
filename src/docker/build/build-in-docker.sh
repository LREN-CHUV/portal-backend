#!/usr/bin/env bash

rm -r target/
mvn package

# Uncomment to generate a PDF API documentation
# mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc
