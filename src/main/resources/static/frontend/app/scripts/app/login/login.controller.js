/**
 * Created by Michael DESIGAUD on 10/09/2015.
 */
angular.module('chuvApp.login').controller('LoginController', ['$scope', '$translatePartialLoader', '$translate', '$rootScope', '$state', 'User', 'backendUrl',
    function ($scope, $translatePartialLoader, $translate, $rootScope, $state, User, backendUrl) {
      $translatePartialLoader.addPart('login');
      $translate.refresh();

      $scope.username = null;
      $scope.password = null;
      $scope.error = false;

      $scope.login = function () {
        User.authenticate($scope.username, $scope.password)
          .then(function(user) {
              if (user !== null) {
                  $state.go('home');
              } else {
                  $scope.error = true;
                  alert("bad credentials");
              }
        });
      };

      $scope.go = function () {
        window.location.href = backendUrl + '/login/hbp';
      };
    }]
);
