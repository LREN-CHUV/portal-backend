/**
 * Created by Michael DESIGAUD on 08/09/2015.
 */
angular.module('chuvApp.components.carrousel').directive("carrousel", [function () {
    return {
        replace: true,
        templateUrl: './scripts/components/carrousel/carrousel-template.html',
        restrict: 'E',
        scope: {
            items: '=',
            onChangeMine:'&'
        },
        link:function(scope,element){

            scope.loadCarrousel = function(){
                var $panel_article_search_widget = $(element);
                if($panel_article_search_widget.length){
                    $panel_article_search_widget.width($panel_article_search_widget.parent().width());
                    var panel_write_article_top_position = $(".panel-form-write-article").offset().top;
                    if(panel_write_article_top_position-$(window).scrollTop() <=100){
                        $panel_article_search_widget.css("top",100);
                    }else{
                        $panel_article_search_widget.css("top",panel_write_article_top_position-$(window).scrollTop());
                    }
                    $panel_article_search_widget.css("bottom","65px");
                }

                var $searchResume = $('.container-ee-search-resume');
                var $listSearch = $searchResume.find('.search-list'),
                    $wrapperListSearch = $listSearch.find(".wrapper-search-list");
                var wList = 0;
                $wrapperListSearch.find('a').each(function() {
                    wList += $(this).outerWidth(true);
                });
                $wrapperListSearch.css({
                    width : wList
                });
            };

            scope.onClickBtnDown = function(event){
                var $element = $(".search-list ul");
                var list_height =  parseFloat($element.height());
                var list_top = parseFloat($element.css('top'));
                var container_height = parseFloat($(".search-list").height());

                if( (list_height+list_top-200) > container_height ){
                    $element.css("top",(list_top-200)+"px");
                    $('.carrousel-up').removeClass("hide");
                }else{
                    $element.css("top",(container_height-list_height)+"px");
                    $(event.currentTarget).addClass("hide");
                }
            };

            scope.onClickBtnUp = function(event){
                var $element = $(".search-list ul");
                var list_top = parseFloat($element.css('top'))

                if( list_top+200 < 0 ){
                    $element.css("top",(list_top+200)+"px");
                    $('.carrousel-down').removeClass("hide");
                }else{
                    $element.css("top","0px");
                    $(event.currentTarget).addClass("hide");
                }
            };

            scope.loadCarrousel();
        }
    };
}]);

angular.module('chuvApp.components.carrousel').directive("carrouselChart", ['Model','backendUrl','$compile','$templateRequest','$q',function (Model,backendUrl,$compile,$templateRequest,$q) {
    return {
       replace: true,
       template: '<img class="btn-draggable" ng-src="{{getUrl()}}" width="120px" height="120px" alt="{{item.title}}"/>',
       restrict: 'E',
       scope:{
         "model":"="
       },
       link:function(scope,element){
           $q.all([Model.get({slug:scope.model.slug}).$promise,$templateRequest("./scripts/app/articles/article-table.html")]).then(function(data){
               scope.dataset = data[0].dataset;
               scope.chartTitle = data[0].title;
               scope.chartUrl = scope.getUrl();

               scope.compiledHtml = $compile(angular.element(data[1]))(scope);

               element.bind('dragstart',function(event){
                   event.originalEvent.dataTransfer.setData('text/html', scope.compiledHtml[0].outerHTML);
               });
           });

           scope.getUrl = function(){
                return backendUrl+"/models/"+scope.model.slug+".svg";
           };
       }
    };
}]);