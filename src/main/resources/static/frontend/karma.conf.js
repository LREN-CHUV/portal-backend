/**
 * Created by Michael DESIGAUD on 11/08/2015.
 */
// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2015-08-11 using
// generator-karma 0.9.0

module.exports = function(config) {
    'use strict';

    config.set({
        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // base path, that will be used to resolve files and exclude
        basePath: './',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'app/bower_components/jquery/dist/jquery.js',
            'app/bower_components/angular/angular.js',
            'app/bower_components/bootstrap/dist/js/bootstrap.js',
            'app/bower_components/angular-animate/angular-animate.js',
            'app/bower_components/angular-cookies/angular-cookies.js',
            'app/bower_components/angular-resource/angular-resource.js',
            'app/bower_components/angular-route/angular-route.js',
            'app/bower_components/angular-sanitize/angular-sanitize.js',
            'app/bower_components/angular-touch/angular-touch.js',
            'app/bower_components/angular-translate/angular-translate.js',
            'app/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'app/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.js',
            'app/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'app/bower_components/angular-ui-router/release/angular-ui-router.js',
            'app/bower_components/tinymce-dist/tinymce.min.js',
            'app/bower_components/angular-ui-tinymce/src/tinymce.js',
            'app/bower_components/highcharts-ng/dist/highcharts-ng.js',
            'app/bower_components/highcharts/highcharts.js',
            'app/bower_components/highcharts/highcharts-more.js',
            'app/bower_components/highcharts/modules/exporting.js',
            'app/bower_components/underscore/underscore.js',
            'app/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'app/bower_components/themify-icons/ie7/ie7.js',
            'app/bower_components/moment/moment.js',
            'app/bower_components/angular-moment/angular-moment.js',
            'app/bower_components/angular-bootstrap-colorpicker/js/bootstrap-colorpicker-module.js',
            'app/bower_components/angular-mocks/angular-mocks.js',
            'app/bower_components/angular-gridster/dist/angular-gridster.min.js',
            'app/bower_components/gsap/src/uncompressed/TweenMax.js',
            'app/bower_components/iCheck/icheck.js',
            'app/bower_components/angular-ui-select/dist/select.js',
            'app/bower_components/gridster/dist/jquery.gridster.with-extras.js',
            'app/bower_components/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.js',
            'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'app/bower_components/jquery.ui/ui/jquery.ui.core.js',
            'app/bower_components/jquery.ui/ui/jquery.ui.widget.js',
            'app/bower_components/jquery.ui/ui/jquery.ui.mouse.js',
            'app/bower_components/jquery.ui/ui/jquery.ui.sortable.js',
            'app/bower_components/angular-utf8-base64/angular-utf8-base64.min.js',
            // endbower
            //Always load modules first
            'app/scripts/app/app.js',
            'app/scripts/**/*module.js',
            'app/scripts/**/*.js',
            //Load tests
            'app/tests/**/*.js'
        ],

        // list of files / patterns to exclude
        exclude: [
            'app/tests/e2e/**'
        ],

        preprocessors: {
            'app/views/**/!(*test).js': 'coverage'
        },

        // web server port
        port: 9999,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: [
            'PhantomJS'
        ],
        reporters: ['junit','dots', 'coverage'],
        junitReporter: {
            outputDir: 'reports',
            outputFile: 'test-results.xml'
        },
        coverageReporter: {
            type : 'cobertura',
            dir : 'reports/',
            file : 'coverage-results.xml'
        },

        // Which plugins to enable
        plugins: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-coverage'
        ],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        colors: true,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO

        // Uncomment the following lines if you are using grunt's server to run the tests
        // proxies: {
        //   '/': 'http://localhost:9000/'
        // },
        // URL root prevent conflicts with the site root
        // urlRoot: '_karma_'
    });
};
