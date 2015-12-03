/**
 * Created by Michael DESIGAUD on 04/09/2015.
 */
angular.module('chuvApp.components.button').directive("roundedButton", function () {
    return {
        replace: true,
        templateUrl: './scripts/components/button/button-rounded-template.html',
        restrict: 'E',
        scope: {
            label: '@',
            onClick:'&',
            iconClass:"@"
        }
    };
});