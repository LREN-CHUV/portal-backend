/**
 * Created by Michael DESIGAUD on 17/08/2015.
 */
'use strict';

describe('Controller: CriteriaController', function () {

    var CriteriaController,
        scope,
        httpBackend,
        rootScope,
        _backendUrl;

    // load the controller's module
    beforeEach(module('chuvApp'));
    beforeEach(module('chuvApp.models'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$httpBackend,backendUrl) {
        rootScope = $rootScope;
        scope = $rootScope.$new();
        httpBackend = $httpBackend;
        _backendUrl = backendUrl;
        CriteriaController = $controller('CriteriaController', {
            $scope: scope
        });

        scope.criterias = [{id:1}];

        httpBackend.when('GET', 'i18n/en/common.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/common.json').respond(200);
        httpBackend.when('GET', 'i18n/en/articles.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/articles.json').respond(200);
        httpBackend.when('GET', 'i18n/en/requests.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/requests.json').respond(200);
        httpBackend.when('GET', 'scripts/app/home/home.html').respond(200);

        httpBackend.flush();
    }));

    /**
     * After each unit test
     */
    /*afterEach(function () {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });

    it('Expect controller initialization to be correct', function () {
        expect(scope.criterias).toBeDefined();
        expect(scope.criterias[0]["id"]).toBeDefined();
        expect(scope.query).toEqual({criterias:[]});
    });

    it('Test criteria selection', function () {
        scope.useCriteria(scope.criterias[0]);
        expect(scope.query.criterias).toEqual([scope.criterias[0]]);
    });

    it('Test isSelected criteria', function () {
        //Selection
        scope.useCriteria(scope.criterias[0]);
        expect(scope.isSelectedCriteria(scope.criterias[0])).toBeTruthy();

        //Un selection
        scope.useCriteria(scope.criterias[0]);
        expect(scope.isSelectedCriteria(scope.criterias[0])).toBeFalsy();
    });*/
});