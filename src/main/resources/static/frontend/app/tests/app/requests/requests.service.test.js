/**
 * Created by Florent PERINEL on 17/08/2015.
 */
'use strict';

describe('Requests services tests', function () {

  // Load service's modules
  beforeEach(module('chuvApp.requests'));

  describe('Request', function () {

    var Request, scope, filter;

    // Initialize the service and a mock scope
    beforeEach(inject(function ($rootScope, _Request_) {
      scope = $rootScope.$new();
      Request = _Request_;
    }));

    it('Search a request in full text', function () {
      var result = Request.search("Alz", null);
      expect(result.results.length).toEqual(3);
    });

    it('Search a request in full text and filter', function () {
      var filters = { "type": [ "BSD" ] };
      var result = Request.search("Alz", filters);
      expect(result.results.length).toEqual(1);
    });

    it('Get a request', function () {
      var result = Request.get(1);
      expect(result.id).toEqual(1);
      expect(result.label).toEqual("Distribution of Alzheimer cases across Europe, between 2000 and 2014");
    });

    it('Create a request', function () {
      var newRequest = {
        label: "Test",
        dataset: "1234567",
        request: {},
        data: {},
        type: "EE",
        chart: {},
        thumbnail: "",
        author: "Henry Markram",
        author_obj: {id: 1}
      };

      var result = Request.create(newRequest);
      expect(result.id).toBeDefined();
    });

    it('Update an request', function () {
      var oldRequest = Request.get(2);
      expect(oldRequest.type).toEqual("EE");

      oldRequest.type = "IA";
      var editedRequest = Request.update(oldRequest);
      expect(editedRequest.type).toEqual("IA");
    });

    it('Remove a request', function () {
      Request.remove(2);
      var oldRequest = Request.get(2);
      expect(oldRequest).toBeNull();
    });

  });

});
