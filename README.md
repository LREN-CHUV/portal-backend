## Introduction

This is the MIP backend.

## Usage

* Build the project: `./build.sh`
* Run the project: `./run.sh`
* Show live logs: `./log.sh` (CTRL+C to quit)
* Stop and remove the running container: `./halt.sh`

## Generate PDF API documentation on build

Uncomment the following line in src/docker/build/build-in-docker.sh :
`mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc`
