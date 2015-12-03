angular.module('chuvApp.hbpapps', ['ngResource','ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
        .state('hbpapps', {
            url: '/hbpapps',
            templateUrl: 'scripts/app/hbpapps/hbpapps.html',
            controller:'HBPAppsController'
        })
    }]);
