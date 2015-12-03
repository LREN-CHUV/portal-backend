
/**
 * Created by Michael DESIGAUD on 11/08/2015.
 */

'use strict';

angular.module('chuvApp.models').config(['$stateProvider',function($stateProvider){

    $stateProvider
        .state('models', {
            url: '/models',
            templateUrl: 'scripts/app/models/model.html',
            controller:'ModelController'
        })
        .state('models-edit', {
            url: '/models/:slug/:isCopy',
            templateUrl: 'scripts/app/models/model.html',
            controller:'ModelController',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('model');
                    return $translate.refresh();
                }]
            }
        });
}]);