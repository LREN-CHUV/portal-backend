/**
 * Created by Florent PERINEL on 14/08/2015.
 */
'use strict';

angular.module('chuvApp.users',

  // Module requirements
  ['ngResource', 'pascalprecht.translate', 'ui.router'])

  // Module configuration
  .config(['$stateProvider', function ($stateProvider) {
    $stateProvider
      .state('user-show', {
        url: '/users/:login',
        templateUrl: 'scripts/app/profile/profile.html',
        controller: 'UserController'
      })
  }]);

