/**
 * Created by Michael DESIGAUD on 09/09/2015.
 */
angular.module('chuvApp.profile').controller('ProfileController', ['$scope', '$translatePartialLoader', '$translate', '$stateParams','User',
  function ($scope, $translatePartialLoader, $translate, $stateParams, User) {

    $scope.user = User.current();

}]);
