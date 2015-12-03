/**
 * Created by Michael DESIGAUD on 15/09/2015.
 */
angular.module('chuvApp.components.toolbar').directive("toolbar", ['$timeout',function ($timeout) {
    return {
        restrict: 'A',
        link:function(scope,element){
            $timeout(function(){
                if($(document).height() - $(window).height() - $("#footer").height()  <  $(window).scrollTop()){
                    $(element).removeClass('fixed');
                } else {
                    $(element).addClass('fixed');
                }
            },500);

        }
    };
}]);