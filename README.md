## Introduction

This is the MIP backend.

## Usage in development environment

* Build and run the project (including clean target): `./go.sh`
* Build the project (including clean target): `./build.sh`
* Run the project: `./run.sh`
* Show live logs: `./log.sh` (CTRL+C to quit)
* Stop and remove the running container: `./halt.sh`
* Clean Maven cache, etc: `./clean.sh`

## Usage for deployments

* Build a versioned image: `./captain_build.sh` or `captain build`
* Build and test an image: `./captain_test.sh` or `captain test`

## Deployment

* See here: https://hub.docker.com/r/hbpmip/portal-backend/

## Generate PDF API documentation on build

Uncomment the following line in src/docker/build/build-in-docker.sh :
`mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc`
