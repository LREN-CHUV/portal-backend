/**
 * Created by Michael DESIGAUD on 12/08/2015.
 */

'use strict';
angular.module('chuvApp.models').controller('ModelController',['$scope','$translatePartialLoader','$translate','$rootScope','Model','$stateParams','ChartUtil',"$state",'$log','User','$timeout',
    function($scope,$translatePartialLoader,$translate,$rootScope,Model,$stateParams,ChartUtil,$state,$log,User,$timeout){

    $translatePartialLoader.addPart('model');
    $translate.refresh();

    $scope.model = {};
    $scope.query = {};
    $scope.dataset = {};
    $scope.chartConfig = {
        options: {
            chart: {
                type: 'column',
                zoomType: 'x'
            }
        },
        size: {
            height: 480
        },
        title:{},
        subtitle:{}
    };

    /**
     * load model by slug
     * @param slug
     */
    $scope.load = function (slug) {
        Model.get({slug: slug}, function(result) {
            $scope.model = result;
            $scope.dataset = result.dataset;
            if($stateParams.isCopy === "true"){
                $scope.model.title = "Copy of "+$scope.model.title;
            }
            $scope.chartConfig.title.text = $scope.model.title;
            $scope.chartConfig.subtitle.text = $scope.model.description;
            $scope.chartConfig.options.chart.type = $scope.model.chart.chartType;
            $scope.chartConfig.xAxis = {code:$scope.model.chart.xAxis};
            $scope.chartConfig.series = _.map($scope.model.chart.chartConfigSets, function(o,idx) {
                var configSet = {};
                configSet.color = o.color;
                configSet.name = o.label;
                configSet.code = o.code;
                return configSet;
            });
            $scope.$emit('event:loadModel',result);
            $scope.$emit('event:searchSuccess',ChartUtil.toChartData($scope.chartConfig,result.dataset));
            $scope.executeBtnAnimate();
            $scope.executed = true;
        });
    };

        if ($stateParams.slug !== undefined) {
            $scope.load($stateParams.slug);
        }


        /**
         * Return true if object has been created by current user
         * @param obj
         * @returns {boolean}
         */
        $scope.isMine = function (obj) {
            return obj.id == null || obj.createdBy.id == User.current().id;
        };

        /**
         * save or update model
         */
    $scope.saveModel = function() {
        $scope.model.chart = {chartType: $scope.chartConfig.options.chart.type};
        $scope.model.chart.xAxis = $scope.chartConfig.xAxis.code;
        $scope.model.chart.svg = $scope.chartConfig.getHighcharts().getSVG();
        $scope.model.chart.chartConfigSets = _.map($scope.chartConfig.series, function (o) {
            var configSet = {};
            configSet.color = o.color;
            configSet.label = o.name;
            configSet.code = o.code;
            return configSet;
        });

            $scope.model.dataset = {code: $scope.dataset.code};
            $scope.model.query = $scope.query;

            $scope.model.title = $scope.chartConfig.title.text;
            $scope.model.description = $scope.chartConfig.subtitle.text;

            if ($scope.model.slug == null) {
                // save new model
                Model.save($scope.model, function (model) {
                    $state.go('models-edit', {slug: model.slug});
                    alert("Save ok");
                },function(){
                    alert("Error on save!");
                });
            } else {
                // save existing model
                Model.update({slug: $scope.model.slug}, $scope.model, function (model) {
                    $state.go('models-edit', {slug: model.slug});
                    alert("Save ok");
                },function(){
                    alert("Error on save!");
                });
            }
        };

        /**
         * Execute animation
         */
        $scope.executeBtnAnimate = function () {
            var searchHelpSelector = '.search-help-container';
            var searchResultSelector = '.search-result';
            var tl = new TimelineMax({ paused: true, onComplete: function () {
                TweenMax.set($(searchHelpSelector), { position: 'absolute'});
                TweenMax.set($(searchResultSelector), { position: 'relative', left: 0, x: 0, y: 0 });
            } });
            tl.fromTo($(searchHelpSelector), 0.3, { scale: 1 }, { scale: 0.8 })
                .fromTo($(searchHelpSelector), 0.3, {  autoAlpha: 1, x: '0%' }, { autoAlpha: 0, x: '40%' })
                .fromTo($(searchResultSelector), 0.3, { scale: 0.8, autoAlpha: 0 }, { scale: 1, autoAlpha: 1 });

            tl.play();
        };

        /**
         * check if list contains value
         * @param list
         * @param value
         * @returns {boolean}
         */
        $scope.contains = function (list, value) {
            var findFunction = function (item) {
                return angular.equals(item, value)
            };

            return _.find(list, findFunction) !== undefined;
        };

        /**
         * Execute a search query
         */
        $scope.executeQuery = function () {
            var query = angular.copy($scope.query);
            //check query
            var error = "";
            //The query must have at less a Variable, a Grouping and a Covariable to be sent to the API.
            if (query.variables.length < 1) {
                error += "The query must have at less a Variable.\n";
            }
            if (query.groupings.length < 1) {
                error += "The query must have at less a Grouping.\n";
            }
            // check if grouping is complete
            if ($scope.contains(query.groupings, {code: undefined})) {
                error += "A grouping is not complete yet.\n";
            }

            if (query.coVariables.length < 1) {
                error += "The query must have at less a Covariable.\n";
            }
            // check if coVariables is complete
            if ($scope.contains(query.coVariables, {code: undefined})) {
                error += "A covariable is not complete yet.\n";
            }

            // check if filter is complete
            if ($scope.contains(query.filters, {operator: '', values: []})) {
                error += "A filter is not complete yet.\n";
            }
            if (error.length > 0) {
                alert(error);
                return;
            }

            // ignore filter without range values
            query.filters = _.filter(query.filters, function (filter) {
                return filter.values.length > 0;
            });

            $scope.executeBtnAnimate();

            Model.executeQuery(query).success(function (queryResult) {
                $scope.executed = true;
                var chartData = ChartUtil.toChartData($scope.chartConfig, queryResult);
                $scope.dataset = chartData.dataset;
                $log.debug("Generating chart with config:", $scope.chartConfig);
                $scope.$emit('event:searchSuccess', chartData);
                var chart = $scope.chartConfig.getHighcharts();
                if (chart) {
                    for (var i = 0; i < chart.series.length; i++) {
                        chart.series[i].show();
                    }
                }
            });

        };

    }]);

/**
 * list models
 */
angular.module('chuvApp.models').controller('ModelsController', ['$scope', '$translatePartialLoader', '$translate', '$rootScope', 'Model', 'backendUrl', '$attrs', 'WidgetService', 'User',
    function ($scope, $translatePartialLoader, $translate, $rootScope, Model, backendUrl, $attrs, WidgetService, User) {

        $translatePartialLoader.addPart('model');
        $translate.refresh();

        var params = {};
        if ($attrs.params !== undefined) {
            params = angular.fromJson($attrs.params);
        }

        $scope.getSvgUrl = function (model) {
            return backendUrl + "/models/" + model.slug + ".svg";
        };

        /**
         * Return true if object has been created by current user
         * @param obj
         * @returns {boolean}
         */
        $scope.isMine = function (obj) {
            return obj.createdBy.id == User.current().id;
        };

        Model.getList(params).then(function (response) {
            $scope.models = response.data;
            WidgetService.make();
        });
    }]);
