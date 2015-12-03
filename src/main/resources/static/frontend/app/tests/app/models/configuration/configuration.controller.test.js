/**
 * Created by Michael DESIGAUD on 17/08/2015.
 */
'use strict';

describe('Controller: ConfigurationController', function () {

    var ConfigurationController,
        scope,
        httpBackend,
        rootScope;

    // load the controller's module
    beforeEach(module('chuvApp'));
    beforeEach(module('chuvApp.models'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
        rootScope = $rootScope;
        scope = $rootScope.$new();
        httpBackend = $httpBackend;
        ConfigurationController = $controller('ConfigurationController', {
            $scope: scope
        });

        scope.chartConfig = {series:[{}]};

        httpBackend.when('GET', 'i18n/en/common.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/common.json').respond(200);
        httpBackend.when('GET', 'i18n/en/articles.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/articles.json').respond(200);
        httpBackend.when('GET', 'i18n/en/requests.json').respond(200);
        httpBackend.when('GET', 'i18n/fr/requests.json').respond(200);
        httpBackend.when('GET', 'scripts/app/home/home.html').respond(200);
        httpBackend.when('GET', 'scripts/app/login/login.html').respond(200);

        httpBackend.flush();
    }));

    /**
     * After each unit test
     */
    afterEach(function () {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });

    it('Test event searchSuccess', function () {
        var data = ['line','bar','column'];
        rootScope.$broadcast('event:searchSuccess', data);
        expect(scope.chartTypes).toEqual(data);
    });
});