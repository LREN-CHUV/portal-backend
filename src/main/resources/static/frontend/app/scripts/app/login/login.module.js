/**
 * Created by Michael DESIGAUD on 10/09/2015.
 */
angular.module('chuvApp.login',

    // Module requirements
    ['ngResource', 'pascalprecht.translate', 'ui.router'])

    // Module configuration
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'scripts/app/login/login.html',
                controller: 'LoginController'
            });
    }]);