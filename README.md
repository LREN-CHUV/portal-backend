## Introduction

This is the MIP implementation.

## Usage

Run `mvn clean`, `mvn package` and `java -jar target/*.jar` to compile and run a demo.
To use it, you need to configure some URLs and register an openid client on the HBP platform.

## API Documentation using Swagger (Springfox)

The API documentation is available at `<BASE URL>/swagger-ui.html`. A JSON version is available at `<BASE URL>/v2/api-docs`

## TODO

* "Dockerize" the project;
* Sync backend with hand written Swagger specs;
* Implement logout;
* Update frontend (add introduction page, hide header/footer when not logged in, remove mock authors).

### Maintenance

* To keep an updated API documentation, the developers should keep synchronized both the auto-generated swagger file (from Java annotations) with the hand written one. You can follow this method to get a YAML description from the Java-annotated code:
  * Add annotations to the Java code;
  * Get JSON from `<BASE URL>/v2/api-docs`;
  * Convert JSON to YAML on [http://jsontoyaml.com](http://jsontoyaml.com).
  