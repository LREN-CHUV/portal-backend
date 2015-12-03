/**
 * Created by Florent PERINEL on 17/08/2015.
 */
'use strict';

angular.module('chuvApp.requests')

/**
 * List and search requests controller
 */
  .controller('RequestsController', ['$scope', '$translatePartialLoader', '$translate', '$filter', 'Request', function ($scope, $translatePartialLoader, $translate, $filter, Request) {

    $scope.query = null;
    $scope.requests = [];
    $scope.facets = [];
    $scope.filters = {};

    /**
     * Initialize controller
     */
    $scope.init = function () {
      $translatePartialLoader.addPart('requests');
      $translate.refresh();
      $scope.refresh();
    };

    /**
     * Search requests with current selected filters
     */
    $scope.refresh = function () {

      if ($scope.query !== null) {
        $scope.query.trim();
      }
      if ($scope.query === "") {
        $scope.query = null;
      }

      var res = Request.search($scope.query, $scope.filters);
      $scope.requests = res.results;
      $scope.filters = res.filters;
      $scope.facets = res.facets;
    };

    /**
     * Add or remove a filter
     *
     * @param facet Facet name
     * @param value Facet value
     */
    $scope.useFilter = function (facet, value) {
      if ($scope.filters[facet] == null) {
        $scope.filters[facet] = [];
      }

      var idx = $scope.filters[facet].indexOf(value);
      if (idx == -1) {
        $scope.filters[facet].push(value);
      } else {
        $scope.filters[facet].splice(idx, 1);
      }
      $scope.refresh($scope.filters);
    };

    /**
     * Check if filter is used
     * @param facet Facet
     * @param value Value
     */
    $scope.isSelectedFilter = function (facet, value) {
      return !($scope.filters[facet] == undefined || $scope.filters[facet].indexOf(value) == -1);
    };

    /**
     * Remove request
     * @param id Id to remove
     */
    $scope.remove = function (id) {
      Request.remove(id);
      $scope.refresh();
    };

    /**
     * Duplicate request
     * @param id
     */
    $scope.duplicate = function (id) {
      var request = JSON.parse(JSON.stringify(Request.get(id)));

      request.id = null;
      request.label = request.label + " (copy)";
      request.date = $filter('date')(new Date(), 'yyyy-MM-ddTHH:mm:ssZ');

      Request.create(request);
      $scope.refresh();
    };

    // Init
    $scope.init();

  }]);
