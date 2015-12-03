/**
 * Created by Michael DESIGAUD on 21/08/2015.
 */
var assert = require('assert');
var English = require('yadda').localisation.English;
browser.ignoreSynchronization = true;

module.exports = (function() {
    return English.library()
        .given("I navigate to url $URL", function(url, next) {
            browser.get(url).then(next);
        })
        .then('i click the My articles link', function(contents, next) {
            element(by.id('myArticles')).click().then(next);
        })
        .then('the url should contain $path', function(path, next) {
            browser.getLocationAbsUrl().then(function(url){
                expect(url).toContain(path);
            }).then(next);
        });
})();