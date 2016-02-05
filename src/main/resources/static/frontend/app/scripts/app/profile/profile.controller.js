/**
 * Created by Michael DESIGAUD on 09/09/2015.
 */
angular.module('chuvApp.profile').controller('ProfileController', ['$scope', '$translatePartialLoader', '$translate', '$stateParams','User','$http',
  function ($scope, $translatePartialLoader, $translate, $stateParams, User, $http) {

    $http.get("/users/"+User.current().username).success(function(data) {$scope.user = data;});

}]);
