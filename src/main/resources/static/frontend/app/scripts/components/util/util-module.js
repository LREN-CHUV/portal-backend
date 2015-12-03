/**
 * Created by Michael DESIGAUD on 26/08/2015.
 */
angular.module('chuvApp.util',['ui.bootstrap']);

/**
 * Register http interceptors to the http provider
 */
angular.module('chuvApp.util').config(['$httpProvider',function ($httpProvider) {
    //Default timeout
    $httpProvider.defaults.timeout = 5000;
    $httpProvider.interceptors.push('httpSecurityInterceptor');
}]);