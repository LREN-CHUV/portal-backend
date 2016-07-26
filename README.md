## Introduction

This is the MIP backend.

## Usage in development environment

* Build and run the project (including clean target): `./go.sh`
* Build the project (including clean target): `./build.sh`
* Run the project: `./run.sh`
* Show live logs: `./log.sh` (CTRL+C to quit)
* Stop and remove the running container: `./halt.sh`
* Clean Maven cache, etc: `./clean.sh`

## Usage in building environment

* Build a versioned image: `./captain_build.sh` (only if `$WORKSPACE=$(pwd)`) or `export WORKSPACE=<path-to-workspace> && captain build`
* Build and test an image: `./captain_test.sh` or `captain test`
* Run the latest image with a non-persistent postgres (only use this for testing purpose): `./captain_run.sh`

## Deployment

* See here: https://hub.docker.com/r/hbpmip/portal-backend/

## Generate PDF API documentation on build

Uncomment the following line in src/docker/build/build-in-docker.sh :
`mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc`

## Tips

* Do not forget to set up the CLIENT_SECRET environment variable when deploying.
