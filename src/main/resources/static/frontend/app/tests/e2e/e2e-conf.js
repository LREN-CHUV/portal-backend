var cucumber = require('protractor-jasmine-cucumber');
var fs = require('fs'), path = require('path');
var screenshotDir = path.resolve('./screenshots/');

function generateUUID() {
    var d = new Date().getTime();
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
}

exports.config = {
    directConnect:false,
    seleniumAddress:'http://localhost:4444/wd/hub',
    specs: ['./yadda/yadda-runner.js'],
    framework: "jasmine2",
    capabilities:{
        'browserName': 'phantomjs',
        'phantomjs.binary.path': require('phantomjs').path,
        'phantomjs.cli.args': ['--debug=true','--webdriver-logfile=webdriver.log','--webdriver-loglevel=INFO'],
        'phantomjs.ghostdriver.cli.args': ['--loglevel=DEBUG']
    },
    jasmineNodeOpts: {
        showColors: true,
        includeStackTrace: true
    },
    onPrepare: function() {
        var jasmineReporters = require('jasmine-reporters');

        jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
            consolidateAll: true,
            savePath: 'reports/testresults',
            filePrefix: 'e2e-tests-results'
        }));

        jasmine.getEnv().addReporter({
            specDone: function(result) {
                // Create screenshots dir if doesn't exist
                if (!fs.existsSync(screenshotDir)) {
                    fs.mkdirSync(screenshotDir);
                }
                var fileName = "/screenshot-"+generateUUID()+".png";
                browser.takeScreenshot().then(function(png) {
                    var file = path.resolve(screenshotDir + fileName);
                    fs.writeFileSync(file, png, {encoding: 'base64'}, console.log);
                });
            }
        });
    }
};