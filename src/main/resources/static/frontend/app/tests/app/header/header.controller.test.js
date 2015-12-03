/**
 * Created by Michael DESIGAUD on 12/08/2015.
 */
'use strict';

describe('Controller: HeaderController', function () {

    var HeaderController,
        scope,
        state,
        httpMock,
        translate;

    // load the controller's module
    beforeEach(module('chuvApp'));
    beforeEach(module('chuvApp.header'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$translate,$state,$httpBackend) {
        scope = $rootScope.$new();
        translate = $translate;
        state = $state;
        httpMock = $httpBackend;
        HeaderController = $controller('HeaderController', {
            $scope: scope
        });
        httpMock.when('GET', 'i18n/en/common.json').respond(200);
        httpMock.when('GET', 'i18n/fr/common.json').respond(200);
        httpMock.when('GET', 'i18n/en/header.json').respond(200);
        httpMock.when('GET', 'i18n/fr/header.json').respond(200);
        httpMock.when('GET', 'scripts/app/home/home.html').respond(200);
    }));

    it('Expect scope.onChangeLanguage to call translate.use', function () {
        spyOn(translate,"use");
        scope.onChangeLanguage("fr");
        expect(translate.use).toHaveBeenCalledWith("fr");
    });

    it('Expect scope.isCurrentLanguage to call translate.use', function () {
        spyOn(translate,"use");
        scope.isCurrentLanguage("en");
        expect(translate.use).toHaveBeenCalled();
    });

    it('Test search method', function () {
        spyOn(state,"go");
        scope.search();
        expect(state.go).toHaveBeenCalledWith("search");
    });
});
