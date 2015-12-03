'use strict';

/**
 *
 * {
 "id": 6,
 "title": "my title",
 "slug": "my-title-4",
 "abstract": "my resume",
 "content": "my content",
 "tags": [],
 "created_at": "2015-08-26T18:10:19+0200",
 "updated_at": "2015-08-26T18:10:19+0200",
 "created_by": "admin",
 "updated_by": "admin"
}
 */

/**
 * @ngdoc overview
 * @name frontendApp
 * @description
 * # frontendApp
 *
 * Main module of the application.
 */
angular
  .module('chuvApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'pascalprecht.translate',
    'ui.tinymce',
    'tmh.dynamicLocale',
    'angularMoment',
    'ui.select',
    'app.config',
    'ab-base64',
    //components
    'chuvApp.components.filters',
    'chuvApp.components.criteria',
    //ui modules
    'chuvApp.header',
    'chuvApp.home',
    'chuvApp.hbpapps',
    'chuvApp.articles',
    'chuvApp.users',
    'chuvApp.requests',
    'chuvApp.models',
    'chuvApp.util',
    'chuvApp.components.header',
    'chuvApp.components.button',
    'chuvApp.components.checkbox',
    'chuvApp.components.carrousel',
    'chuvApp.components.scrollbar',
    'chuvApp.components.widget',
    'chuvApp.mydata',
    'chuvApp.profile',
    'chuvApp.login',
    'chuvApp.components.toolbar'
  ])
  .config(['$translateProvider','tmhDynamicLocaleProvider','backendExportChartUrl', function ($translateProvider,tmhDynamicLocaleProvider,backendExportChartUrl) {
    $translateProvider.useLoader('$translatePartialLoader', {
      urlTemplate: 'i18n/{lang}/{part}.json'
    });

    $translateProvider.preferredLanguage('en');
    $translateProvider.fallbackLanguage('en');

    $translateProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
    $translateProvider.useSanitizeValueStrategy('escaped');

    tmhDynamicLocaleProvider.localeLocationPattern('i18n/angular-locale_{{locale}}.js');
    tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');

    // set global configuration highcharts
    Highcharts.setOptions({
        exporting: {
            url:backendExportChartUrl
        }
    });
  }])
  .run(['$state','$translatePartialLoader','$translate', 'amMoment', '$rootScope','backendUrl','$cookies','User',
    function($state,$translatePartialLoader,$translate, amMoment,$rootScope,backendUrl,$cookies,User){
        $translatePartialLoader.addPart('common');
        $translate.refresh();
        amMoment.changeLocale('en');
        $state.go('login');

        $rootScope.getPdfUrl = function(slug){
            return backendUrl+"/articles/"+slug+".pdf";
        };

        $rootScope.$on('$stateChangeStart',function(){
            if(User.hasCurrent()){
                $rootScope.user = User.current();
            }
        });

        /**
         * Check if is a new visitor or not
         */
        $rootScope.isNewVisitor = function() {
            if(User.hasCurrent()) {
                return $cookies.get('intro-' + User.current().username) !== "hide";
            }
            return true;
        };
    }]);
