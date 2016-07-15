## Introduction

This is the MIP backend.

## Usage

* Build and run the project (including clean target): `./go.sh`
* Build the project (including clean target): `./build.sh`
* Run the project: `./run.sh`
* Show live logs: `./log.sh` (CTRL+C to quit)
* Stop and remove the running container: `./halt.sh`
* Clean Maven cache, etc: `./clean.sh`

## Generate PDF API documentation on build

Uncomment the following line in src/docker/build/build-in-docker.sh :
`mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc`
