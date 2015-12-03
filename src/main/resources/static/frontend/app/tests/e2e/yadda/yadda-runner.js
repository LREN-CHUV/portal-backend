/**
 * Created by Michael DESIGAUD on 21/08/2015.
 */
var exports = module.exports = {};

exports.runner = (function () {
    var Yadda = require('yadda');
    Yadda.plugins.jasmine.StepLevelPlugin.init();

    new Yadda.FeatureFileSearch('./app/tests/e2e/yadda/features').each(function(file) {

        featureFile(file, function(feature) {
            var a = file.match(/[a-zA-Z0-9-_]*\.feature/gi)[0],
                name = a.replace(".feature","");

            var library = require('./steps/'+name+'-steps');
            var yadda = Yadda.createInstance(library);

            scenarios(feature.scenarios, function(scenario) {
                steps(scenario.steps, function(step, done) {
                    yadda.run(step, done);
                    done();
                });
            });
        });
    });
}());