/**
 * Created by Michael DESIGAUD on 09/09/2015.
 */
angular.module('chuvApp.users').controller('UserController', ['$scope', '$translatePartialLoader', '$translate', '$stateParams','User','$http',
  function ($scope, $translatePartialLoader, $translate, $stateParams, User, $http) {

    $http.get("/users/"+$stateParams.username).success(function(data) {$scope.user = data;});

}]);
