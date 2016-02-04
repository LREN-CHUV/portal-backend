/**
 * Created by Michael DESIGAUD on 11/08/2015.
 */
angular.module('chuvApp.header').controller('HeaderController', ['$scope', '$translate', '$translatePartialLoader', '$state', 'tmhDynamicLocale', 'User','$rootScope','$http',
  function ($scope, $translate, $translatePartialLoader, $state, tmhDynamicLocale, User,$rootScope, $http) {

    $translatePartialLoader.addPart('header');
    $translate.refresh();

    /**
     * Change language event
     * @param lang new lang
     */
    $scope.onChangeLanguage = function (lang) {
      $translate.use(lang);
      tmhDynamicLocale.set(lang);
    };

    /**
     * Is current language
     * @param languageKey language key
     * @return {boolean} true if language is the same
     */
    $scope.isCurrentLanguage = function (languageKey) {
      return $translate.use() === languageKey;
    };

    /**
     * Search method event
     */
    $scope.search = function () {
      $state.go('search');
    };

   $scope.logout = function(){
       $http.get("/logout");
       User.removeCurrent();
       $state.go('login');
       $rootScope.user = null;
   };
  }]);
