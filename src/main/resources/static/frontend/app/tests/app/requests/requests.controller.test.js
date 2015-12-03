/**
 * Created by Florent PERINEL on 17/08/2015.
 */
'use strict';

describe('Requests controllers tests', function () {

  // Load controller's modules
  beforeEach(module('chuvApp'));
  beforeEach(module('chuvApp.requests'));

  describe('RequestsController', function () {

    var RequestsController, scope, translate;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($rootScope, $controller, $translate) {
      scope = $rootScope.$new();
      translate = $translate;
      RequestsController = $controller('RequestsController', {$scope: scope});
    }));

    it('Search all requests', function () {
      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(4);
      expect(scope.filters.type).toBeUndefined();
    });

    it('Search only EE type requests', function () {

      scope.useFilter('type', 'EE');

      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(2);
      expect(scope.filters.type.length).toEqual(1);
    });

    it('Search only EE type requests for Lucas Laursen', function () {

      scope.useFilter('type', 'EE');
      scope.useFilter('author', 'Lucas Laursen');

      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(0);
      expect(scope.filters.type.length).toEqual(1);
      expect(scope.filters.author.length).toEqual(1);
    });

    it('Deselect filter', function () {

      scope.useFilter('type', 'EE');
      scope.useFilter('author', 'Lucas Laursen');

      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(0);
      expect(scope.filters.type.length).toEqual(1);
      expect(scope.filters.author.length).toEqual(1);

      scope.useFilter('author', 'Lucas Laursen');

      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(2);
      expect(scope.filters.type.length).toEqual(1);
      expect(scope.filters.author.length).toEqual(0);
    });

    it('Search with full text all EE type requests', function () {

      scope.useFilter('type', 'EE');
      scope.query = "Alz";

      scope.refresh();

      expect(scope.facets.length).toEqual(2);
      expect(scope.requests.length).toEqual(2);
    });

    it('Remove an request', function () {
      expect(scope.requests.length).toEqual(4);
      scope.remove(1);
      expect(scope.requests.length).toEqual(3);
    });

    it('Duplicate a request', function () {
      expect(scope.requests.length).toEqual(4);
      scope.duplicate(1);
      expect(scope.requests.length).toEqual(5);
    });

  });

});
