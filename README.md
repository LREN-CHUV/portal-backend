## Introduction

This is the MIP implementation.

## Usage

Build the project with `./build` and run it with `./run`.

## API Documentation using Swagger (Springfox)

The API documentation is available at `<BASE URL>/swagger-ui.html`. A JSON version is available at `<BASE URL>/v2/api-docs`

## TODO

* Externalize configuration (DB parameters, security enabled/disabled, ...);
* Sync backend with hand written Swagger specs (see Maintenance section below);
* Implement logout;
* Add some details to the group and variable models like descriptions;
* Update frontend (add introduction page, hide header/footer when not logged in, remove mock authors, real stats like users count);
* Fix bugs;
* Add SoapUI tests.

## BUGS

* When adding a model to an article, the data table should be attached to the SVG plot;
* MyData and MyCommunity does not show articles when adding a model;
* Opening an existing model should auto-populate drop-down fields and execute query;
* `See more` buttons do not work (homepage), neither `Articles` and `Models` from the profile page;
* The '3 own models' tiles does not show on the homepage.


### Maintenance

* To keep an updated API documentation, the developers should keep synchronized both the auto-generated swagger file (from Java annotations) with the hand written one. You can follow this method to get a YAML description from the Java-annotated code:
  * Add annotations to the Java code;
  * Get JSON from `<BASE URL>/v2/api-docs`;
  * Convert JSON to YAML on [http://jsontoyaml.com](http://jsontoyaml.com).
  