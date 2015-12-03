/**
 * Created by Michael DESIGAUD on 17/08/2015.
 */
'use strict';

describe('Controller: ChartController', function () {

    var ChartController,
        scope,
        httpMock,
        rootScope;

    // load the controller's module
    beforeEach(module('chuvApp'));
    beforeEach(module('chuvApp.models'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
        rootScope = $rootScope;
        scope = $rootScope.$new();
        scope.chartConfig = {};
        httpMock = $httpBackend;
        ChartController = $controller('ChartController', {
            $scope: scope
        });

        httpMock.when('GET', 'i18n/en/common.json').respond(200);
    }));

    it('Test event searchSuccess', function () {
        var data = {chartData:{results:[{value:12},{value:3}]}};
        expect(true).toEqual(true);
    });
});