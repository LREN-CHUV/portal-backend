/**
 * Created by Florent PERINEL on 14/08/2015.
 */
'use strict';

angular.module('chuvApp.requests',

  // Module requirements
  ['ngResource', 'pascalprecht.translate', 'ui.router'])

  // Module configuration
  .config(['$stateProvider', function ($stateProvider) {
    $stateProvider
      .state('requests', {
        url: '/requests',
        templateUrl: 'scripts/app/requests/requests-list.html',
        controller: 'RequestsController'
      })
  }]);

