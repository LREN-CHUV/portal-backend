/**
 * Created by Michael DESIGAUD on 17/08/2015.
 */
'use strict';

describe('Controller: ModelController', function () {

    var ModelController,
        scope,
        rootScope,
        httpMock,
        stateParams,
        ModelService,
        _backendUrl,
        state,
        translate;

    // load the controller's module
    beforeEach(module('chuvApp'));
    beforeEach(module('chuvApp.models'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$translate,$httpBackend,backendUrl,$stateParams,$state) {
        rootScope = $rootScope;
        scope = $rootScope.$new();
        translate = $translate;
        stateParams = $stateParams;
        state = $state;
        httpMock = $httpBackend;
        _backendUrl = backendUrl;
        ModelController = $controller('ModelController', {
            $scope: scope
        });

        httpMock.when('GET', 'i18n/en/common.json').respond(200);
        httpMock.when('GET', 'i18n/fr/common.json').respond(200);
        httpMock.when('GET', 'i18n/en/model.json').respond(200);
        httpMock.when('GET', 'i18n/fr/model.json').respond(200);
        httpMock.when('GET', 'scripts/app/home/home.html').respond(200);
        httpMock.when('GET', 'scripts/app/models/model.html').respond(200);

    }));

    it('Expect scope.chartConfig to be defined', function () {
        expect(scope.model).toBeDefined();
        expect(scope.chartQuery).toBeDefined();
        expect(scope.dataset).toBeDefined();
        expect(scope.chartConfig).toBeDefined();
        expect(scope.chartConfig.options).toBeDefined();
        expect(scope.chartConfig.options.chart).toBeDefined();
        expect(scope.chartConfig.xAxis).not.toBeDefined();
    });

    it('Event searchSuccess should be handled', function () {
        var queryResult = {dataset:{"code":"m6bolp1iyz48skocw44ss8kk84skw0o","date":"2015-09-08T14:36:59+0200",
            "header":["3rdVentricle","ADAS11","ADAS13"],
            "data":{"3rdVentricle":[2.6984749,2.7465396,2.733165,2.8529217,1.5337946,1.5230918,1.5609943,1.6304724,1.7005655,1.0883133,1.0962301,1.2159779],
                "ADAS11":[22,19,24,25.67,8.67,11,5.67,7,6.67,12.33,14.33,23.33],"ADAS13":[31,30,35,37.67,14.67,15,"",11,11.67,24.33,24.33,35.33]}}};
        httpMock.expectPOST(_backendUrl+'/queries/requests.json').respond(queryResult.dataset);
        scope.executeQuery();
        httpMock.flush();

        expect(scope.executed).toBe(true);
        expect(scope.dataset).toEqual(queryResult.dataset);
    });

    it('Event chartTypeChanged should be handled', function () {
        var data = {type:'column'};
        rootScope.$broadcast('event:chartTypeChanged',data);
        expect(scope.chartConfig.options.chart.type).toEqual(data.type);
    });

    it('should be save', function() {
        var model ={};
        scope.chartConfig.xAxis = {code:"code"};
        scope.chartConfig.getHighcharts = function(){
            var chart = {};

            chart.getSVG = function(){
                return null;
            };
            return chart;
        };
        scope.model = model;
        spyOn(state,'go');

        httpMock.expectPOST(_backendUrl+'/models').respond(model);
        scope.saveModel();
        httpMock.flush();
        expect(state.go).toHaveBeenCalled();
    });

    describe('on existing model', function() {
        var model = {"id":2,"title":"dej","slug":"dej",
            "query":{"id":2,"variables":[],"coVariables":[{"code":"3rdVentricle"},{"code":"ADAS11"},{"code":"ADAS13"}],"groupings":[],"filters":[]},
            "dataset":{
                "id":11,"code":"n65ozedqdzkc8wc0kckc8ss0c88ks8w","date":"2015-09-03T15:30:15+0200",
                "header":["3rdVentricle","ADAS11","ADAS13"],
                "data":{"3rdVentricle":[],"ADAS11":[],"ADAS13":[]}
            },
            "chart":{"id":2,"chartType":"column","xAxis":"3rdVentricle",
                "chartConfigSets":[{"id":3,"code":"ADAS11","label":"ADAS-11","color":"#7cb5ec"},
                                    {"id":4,"code":"ADAS13","label":"ADAS-13","color":"#434348"}],
                "svg":"<svg>"}
        };
        // Initialize the controller and a mock scope
        beforeEach(inject(function ($controller,$rootScope,$stateParams,Model,$httpBackend,backendUrl,$state) {
            rootScope = $rootScope;
            scope = $rootScope.$new();
            stateParams = $stateParams;
            stateParams.slug = model.slug;
            _backendUrl = backendUrl;
            state = $state;
            ModelService = Model;
            spyOn(scope,'$emit');

            httpMock.expectGET(_backendUrl+'/models/'+encodeURI(model.slug)).respond(model);

            ModelController = $controller('ModelController', {
                $scope: scope
            });
            httpMock.flush();
        }));

        /**
         * After each unit test
         */
        afterEach(function() {
            httpMock.verifyNoOutstandingExpectation();
            httpMock.verifyNoOutstandingRequest();
        });

        it('should be loading', function() {
            expect(scope.chartConfig.xAxis.code).toEqual("3rdVentricle");
            expect(scope.chartConfig.title.text).toEqual(model.title);
            expect(scope.chartConfig.options.chart.type).toEqual(model.chart.chartType);
            expect(scope.chartConfig.series[0]).toEqual({"code":"ADAS11","name":"ADAS-11","color":"#7cb5ec", data: [  ] });
            expect(scope.chartConfig.series[1]).toEqual({"code":"ADAS13","name":"ADAS-13","color":"#434348", data: [  ] });

            expect(scope.$emit).toHaveBeenCalledWith('event:searchSuccess',{ dataset: model.dataset});
        });

        it('should be save', function() {
            scope.chartConfig.getHighcharts = function(){
                var chart = {};

                chart.getSVG = function(){
                    return null;
                };
                return chart;
            };
            scope.model = model;

            spyOn(state,'go');

            httpMock.expectPUT(_backendUrl+'/models/'+encodeURI(model.slug)).respond(model);
            scope.saveModel();
            httpMock.flush();
            expect(state.go).toHaveBeenCalled();
        });
    });
});