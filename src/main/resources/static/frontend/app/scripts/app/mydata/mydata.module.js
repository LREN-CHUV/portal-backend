/**
 * Created by Michael DESIGAUD on 08/09/2015.
 */
'use strict';

angular.module('chuvApp.mydata',

    // Module requirements
    ['ngResource', 'pascalprecht.translate', 'ui.router'])

    // Module configuration
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('data', {
                url: '/data/:scope',
                templateUrl: 'scripts/app/mydata/mydata.html',
                controller: 'MyDataController'
            });
    }]);