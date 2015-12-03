/**
 * Created by Michael DESIGAUD on 04/09/2015.
 */
'use strict';

angular.module('chuvApp.models').controller('ModelFooterController',['$scope','Model','$state',function($scope,Model,$state) {

    /**
     * export chart in pdf file
     */
    $scope.exportPdf = function () {
        var highChart = $scope.chartConfig.getHighcharts();
        if (highChart === undefined) {
            alert("no chart available!");
        }

        highChart.exportChart({
            type: 'application/pdf',
            filename: $scope.model.slug
        });
    };

    $scope.copyModel = function () {
        Model.copy($scope.model).success(function (modelCopy) {
            $state.transitionTo($state.current, {slug: modelCopy.slug, isCopy: "true"}, {
                reload: true, inherit: true, notify: true
            });
        });
    };
}]);
