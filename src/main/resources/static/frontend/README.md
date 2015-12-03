# CHUV frontend v0.0.1

## Libraries
- AngularJS v1.4.3
- Bootstrap v3.2.0

## Build & development

- Run npm install to retrieve all node dependencies
- Run "bower install" to retrieve all bower dependencies
- Run "grunt build" to build the project
- Run "grunt serve" to run the local server
- Run "npm install -g protractor" to install protractor and webdriver-manager
- Run "node ./node_modules/grunt-protractor-runner/node_modules/protractor/bin/webdriver-manager update --standalone"

## Errors
- If "unable to locate grunt" => Run "npm install -g grunt-cli", "npm install", "grunt build", "grunt serve"
- If "Task "karma" not found" => Run "npm install grunt-karma --save-dev"


## Testing

Running `grunt test` will run the unit tests with karma.
