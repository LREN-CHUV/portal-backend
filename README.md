## Introduction

This is the MIP implementation.

## Usage

Build the project with `./build` and run it with `./run`.

## API Documentation using Swagger (Springfox)

The API documentation is available at `<BASE URL>/swagger-ui.html`. A JSON version is available at `<BASE URL>/v2/api-docs`

## TODO

* Implement SoapUI tests;
* Clean code (both back-end front-end);
* Sync with original Swagger description;
* Externalize configuration (DB parameters, security enabled/disabled, ...);
* Make user tests with multiple users;
* Fix bugs.

## BUGS

* Export PDF does not work;

### Maintenance

* To keep an updated API documentation, the developers should keep synchronized both the auto-generated swagger file (from Java annotations) with the hand written one. You can follow this method to get a YAML description from the Java-annotated code:
  * Add annotations to the Java code;
  * Get JSON from `<BASE URL>/v2/api-docs`;
  * Convert JSON to YAML on [http://jsontoyaml.com](http://jsontoyaml.com).
  