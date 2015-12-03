/**
 * Created by Michael DESIGAUD on 09/09/2015.
 */
angular.module('chuvApp.profile', ['ngResource','pascalprecht.translate','ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('profile', {
                url: '/profile',
                templateUrl: 'scripts/app/profile/profile.html',
                controller: 'ProfileController'
            })
    }]);