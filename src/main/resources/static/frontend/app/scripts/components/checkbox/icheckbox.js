/**
 * Created by Michael DESIGAUD on 07/09/2015.
 */
angular.module('chuvApp.components.checkbox').directive("icheckbox", [function () {
    return {
        replace: true,
        template: '<label><input type="checkbox" ng-model="ngModel" ng-checked="checked" />{{label}}</label>',
        restrict: 'E',
        require:'ngModel',
        scope: {
            label: '@',
            checked:'=?',
            ngModel:'='
        },
        link:function(scope,element){
            $(element).iCheck({
                checkboxClass: 'icheckbox_minimal-red',
                radioClass: 'iradio_minimal-red'
            });
        }
    };
}])
    .directive('icheck', ['$timeout', function($timeout) {
    'use strict';
    return {
        require: '?ngModel',
        link: function($scope, element, $attrs, ngModel) {
            return $timeout(function() {
                var parentLabel = element.parent('label');
                if (parentLabel.length) {
                    parentLabel.addClass('icheck-label');
                }
                var value;
                value = $attrs.value;

                $scope.$watch($attrs.ngModel, function() {
                    angular.element(element).iCheck('update');
                });

                return angular.element(element).iCheck({
                    checkboxClass: $attrs.icheck.length? 'icheckbox_'+$attrs.icheck:'icheckbox_minimal-blue',
                    radioClass: $attrs.icheck.length? 'iradio_'+$attrs.icheck:'iradio_minimal-blue'
                }).on('ifChanged', function(event) {
                    if (angular.element(element).attr('type') === 'checkbox' && $attrs.ngModel) {
                        $scope.$apply(function() {
                            return ngModel.$setViewValue(event.target.checked);
                        });
                    }
                    if (angular.element(element).attr('type') === 'radio' && $attrs.ngModel) {
                        return $scope.$apply(function() {
                            return ngModel.$setViewValue(value);
                        });
                    }
                });
            });
        }
    };
}]);