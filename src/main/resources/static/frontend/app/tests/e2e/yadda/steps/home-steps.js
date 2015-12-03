/**
 * Created by Michael DESIGAUD on 21/08/2015.
 */
var assert = require('assert');
var English = require('yadda').localisation.English;
browser.ignoreSynchronization = true;

module.exports = (function() {
    return English.library()
        .given("I navigate to $URL", function(url, next) {
            browser.get(url).then(next);
        })
        .then('the title should be $TITLE', function(contents, next) {
            browser.getTitle()
                .then(function(title){
                    expect(title).toContain(contents);
                })
                .then(next);
        });
})();