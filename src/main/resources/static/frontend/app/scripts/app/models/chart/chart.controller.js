
'use_strict';

angular.module('chuvApp.models').controller('ChartController',['$scope','$timeout',function($scope,$timeout){

    $scope.showTable = true;

    $scope.hiddenData = true;

    $scope.maxCol = 6;
    $scope.maxRow = 5;

    /**
     * Show table animation
     */
    $scope.showTable = function(){
        TweenMax.fromTo($(".panel-charts-graph"), 0.3, {scale : 1, autoAlpha : 1 }, {scale : 0.8, autoAlpha : 0 });
        TweenMax.fromTo($(".panel-data-graph"), 0.3, {scale : 0.8, autoAlpha : 0 }, {scale : 1, autoAlpha : 1 });
        $scope.hiddenData = false;
    };

    /**
     * Close table animation
     */
    $scope.closeTable = function(){
        TweenMax.fromTo($('.panel-data-graph'), 0.3, {scale : 1, autoAlpha : 1 }, {scale : 0.8, autoAlpha : 0 });
        TweenMax.fromTo($(".panel-charts-graph"), 0.3, {scale : 0.8, autoAlpha : 0 }, {scale : 1, autoAlpha : 1 });
        $scope.hiddenData = true;
        var chart = $scope.chartConfig.getHighcharts();
        if (chart) {
            for (var i = 0; i < chart.series.length; i++) {
                chart.series[i].show();
            }
        }
    };
}]);
