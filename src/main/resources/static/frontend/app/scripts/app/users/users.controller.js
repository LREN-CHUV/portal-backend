/**
 * Created by Michael DESIGAUD on 09/09/2015.
 */
angular.module('chuvApp.users').controller('UserController', ['$scope', '$translatePartialLoader', '$translate', '$stateParams','User',
  function ($scope, $translatePartialLoader, $translate, $stateParams, User) {

    $scope.user = User.get($stateParams.login);

}]);
