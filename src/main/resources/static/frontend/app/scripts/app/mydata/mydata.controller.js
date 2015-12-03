/**
 * Created by Michael DESIGAUD on 08/09/2015.
 */
'use strict';

angular.module('chuvApp.mydata').controller('MyDataController', ['$scope', '$translatePartialLoader', '$translate','Article','backendUrl','Model','$q','ChartUtil','$timeout','ModalUtil','WidgetService','$stateParams','User',
        function ($scope, $translatePartialLoader, $translate,Article,backendUrl,Model,$q,ChartUtil,$timeout,ModalUtil,WidgetService,$stateParams,User) {
            $translatePartialLoader.addPart('mydata');
            $translate.refresh();

        $scope.rows = [];

        $scope.isMyDataScope = $stateParams.scope === "mydata";

        $scope.init = function(){
            var paramsArticle = {team:$scope.isMyDataScope ? 0 : 1, own:!$scope.isMyDataScope ? 0 : 1};
            if(!$scope.isMyDataScope){
                paramsArticle.status = "published";
            }

            var paramsModel = {team:$scope.isMyDataScope ? 0 : 1, own:!$scope.isMyDataScope ? 0 : 1};
            if(!$scope.isMyDataScope){
              paramsModel.valid = "1";
            }

            $q.all([Article.query(paramsArticle).$promise,
                Model.query(paramsModel).$promise]).then(function(data){
                var articles = data[0];
                var models = data[1];

                if(!models && articles){
                    angular.forEach(articles,function(article){
                        $scope.rows.push({type: 'A', data: article, size: 2, row: index + 1, col: 2});
                    });
                } else {
                    var promises = [];
                    angular.forEach(models, function (model) {
                        promises.push($scope.getModel(model.slug));
                    });

                    $q.all(promises).then(function (modelsData) {
                        angular.forEach(modelsData,function(model,index){
                            $scope.rows.push({type: 'M', data: model, size: 1, row: index+1, col: 1});
                            if(articles[index]) {
                                $scope.rows.push({type: 'A', data: articles[index], size: 2, row: index + 1, col: 2});
                            }
                        });
                        if(articles.length > modelsData.length){
                            angular.forEach(articles.slice(modelsData.length),function(article,index){
                                $scope.rows.push({type: 'A', data: article, size: 2, row: index + 1, col: 2});
                            });
                        }
                        $scope.refresh();
                    });
                }
            });
        };

       $scope.getModel = function(slug){
           var deferred = $q.defer();
           Model.get({slug:slug}).$promise.then(function(model){
               model.chartConfig = {
                   options: {
                       chart: {
                           zoomType: 'x',
                           height:'280'
                       }
                   },
                   title:{},
                   subtitle:{}
               };
               model.chartConfig.title.text = model.title;
               model.chartConfig.subtitle.text = model.description;
               model.chartConfig.options.chart.type = model.chart.chartType;
               model.chartConfig.xAxis = {code:model.chart.xAxis};
               model.chartConfig.series = _.map(model.chart.chartConfigSets, function(o) {
                   var configSet = {};
                   configSet.color = o.color;
                   configSet.name = o.label;
                   configSet.code = o.code;
                   return configSet;
               });
               ChartUtil.toChartData(model.chartConfig,model.dataset);
               deferred.resolve(model);
           });
           return deferred.promise;
       };

        $scope.init();

       /**
        * Return true if object has been created by current user
        * @param obj
        * @returns {boolean}
        */
       $scope.isMine = function(obj) {
         return obj.createdBy.id == User.current().id;
       };

       $scope.refresh = function(){
           $timeout(function(){
               $("div.gridster").gridster({
                   namespace: 'div.gridster',
                   widget_selector: "div.grid-item",
                   widget_margins: [10, 10],
                   widget_base_dimensions: [340, 360],
                   max_cols : 1,
                   max_rows : 1
               }).data('gridster');
               $scope.$broadcast('highchartsng.reflow');
               WidgetService.make();
           },500);
       };

      $scope.showArticleModal = function(article){
          ModalUtil.showModal($scope,article);
      };
    }]
);
