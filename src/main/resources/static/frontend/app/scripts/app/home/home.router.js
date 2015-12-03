/**
 * Created by Michael DESIGAUD on 11/08/2015.
 */
angular.module('chuvApp.home').config(['$stateProvider',function($stateProvider){

    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: 'scripts/app/home/home.html',
            controller:'HomeController'
        })
}]);