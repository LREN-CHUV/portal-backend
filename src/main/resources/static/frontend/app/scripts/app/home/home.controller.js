/**
 * Created by Michael DESIGAUD on 11/08/2015.
 */
angular.module('chuvApp.home').controller('HomeController',['$scope','$translatePartialLoader','$translate','WidgetService','$rootScope','User','$cookies','Model','ChartUtil','$q',
  function($scope,$translatePartialLoader,$translate,WidgetService,$rootScope,User,$cookies,Model,ChartUtil,$q){

    $translatePartialLoader.addPart('home');
    $translatePartialLoader.addPart('articles');
    $translatePartialLoader.addPart('requests');
    $translate.refresh();

    $scope.datas = {
      scientists: 14,
      articles: 73,
      patients: 3721,
      teams: 126
    };

    $scope.models = [];

    /**
     * Search articles with current selected filters
     */
    $scope.init = function () {
        var introTxtSelector = '.intro-txt';
        $scope.tlIntro = new TimelineMax({ paused : true });
        $scope.tlIntro .to($('.intro'), 0.6, { y : '-100%', scale : .4, autoAlpha : 0, ease : Power4.easeIn })
            .to($(introTxtSelector), 0.6, { y : '0%', scale : 1, autoAlpha : 1, ease : Power4.easeOut });

        $scope.tlIntro2 = new TimelineMax({ paused : true });
        $scope.tlIntro2.to($(introTxtSelector), 0.6, { y : '-100%', scale : .4, autoAlpha : 0, ease : Power4.easeIn }, 'start')
            .to($('.trigger-intro'), 0.3, { autoAlpha : 0 }, 'start')
            .to($('.trigger-close-intro'), 0.3, { autoAlpha : 0 }, 'start')
            .to($('.intro-container'), 0.2, { height: 0 }, '-=0.2');

      Model.getList({ limit:3, team:0, own:1}).then(function(response){
        $scope.results = response.data;
        var promises = [];

        angular.forEach($scope.results, function(model) {
          promises.push($scope.getModel(model.slug));
        });

        $q.all(promises).then(function (modelsData) {
          angular.forEach(modelsData,function(model){
            $scope.models.push(model);
          });
        });

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

    $scope.gridsterOpts = {
        resizable: {
            enabled: false
        }
    };

    $scope.animateIntro = function(){
        if( !$scope.step2 ) {
            $scope.tlIntro.play();
            $scope.step2 = true;
        }
        else {
            $scope.tlIntro2.play();
          $scope.hideIntro();
        }
    };

    $scope.closePanel = function(event){
        $(event.currentTarget).fadeOut();
        TweenMax.to($('.intro'), 0.6, { y : '-100%', scale : .4, autoAlpha : 0, ease : Power4.easeIn });
        $scope.tlIntro2.play();
        $scope.hideIntro();
    };



    /**
     * Hide intro and store in cookie
     */
    $scope.hideIntro = function() {
      $cookies.put('intro-' + User.current().username, "hide");
    };

    // Init the controller values
    $scope.init();

  }]);
