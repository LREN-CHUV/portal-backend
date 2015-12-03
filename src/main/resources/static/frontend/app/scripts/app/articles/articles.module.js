/**
 * Created by Florent PERINEL on 12/08/2015.
 */
'use strict';

angular.module('chuvApp.articles',

  // Module requirements
  ['ngResource', 'pascalprecht.translate', 'ui.router'])

  // Module configuration
  .config(['$stateProvider', function ($stateProvider) {
    $stateProvider
      .state('articles', {
        url: '/articles',
        templateUrl: 'scripts/app/articles/articles-list.html',
        controller: 'ArticlesController'
      })
      .state('articles-create', {
        url: '/articles/create',
        templateUrl: 'scripts/app/articles/articles-form.html',
        controller: 'ArticleController'
      })
      .state('articles-edit', {
        url: '/articles/:action/:slug',
        templateUrl: 'scripts/app/articles/articles-form.html',
        controller: 'ArticleController'
      })
  }]);

