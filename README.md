## Introduction

This is the MIP implementation.

## Usage

Run `mvn clean`, `mvn package` and `java -jar target/*.jar` to compile and run a demo.
To use it, you need to configure some URLs and register an openid client on the HBP platform.

## API Documentation using Swagger (Springfox)

The API documentation is available at `<BASE URL>/swagger-ui.html`. A JSON version is available at `<BASE URL>/v2/api-docs`

## TODO

* Clean swagger annotations (ignore some parameters);
* Implement missing API features (updates, deletes, special parameters);
* Implement logout;
* Adapt frontend (add introduction page, hide header/footer when not logged in, remove mock authors).
