## Introduction

This is the MIP implementation.

## Usage

Build the project with `./build` and run it with `./run`.

## API Documentation using Swagger (Springfox)

The API documentation is available at `<BASE URL>/swagger-ui.html`. A JSON version is available at `<BASE URL>/v2/api-docs`

## TODO

* Fix bugs;
* Add author's view if needed;
* Implement delete methods if needed;
* Externalize configuration (DB parameters, security enabled/disabled, ...);
* Implement SoapUI tests;
* Clean code (both back-end front-end);
* Sync with original Swagger description;
* Make user tests with multiple users.

## BUGS

* In the profile view, we shouldn't see the '+' between firstname and lastname;
* Export PDF;

### Maintenance

* To keep an updated API documentation, the developers should keep synchronized both the auto-generated swagger file (from Java annotations) with the hand written one. You can follow this method to get a YAML description from the Java-annotated code:
  * Add annotations to the Java code;
  * Get JSON from `<BASE URL>/v2/api-docs`;
  * Convert JSON to YAML on [http://jsontoyaml.com](http://jsontoyaml.com).
  