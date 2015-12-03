/**
 * Created by Michael DESIGAUD on 03/09/2015.
 */
angular.module('chuvApp.components.header').directive("changeOnScroll", ['$window',function ($window) {
    return function(scope,element) {
        angular.element($window).bind("scroll", function() {
            var top = $('body').scrollTop();
            if(top === 0){
                $(element).removeClass('fixed');
            } else{
                $(element).addClass('fixed');
            }
        });
    };
}]);