/**
 * This filter can be used to highlight search term on result list
 */
'use strict';

angular.module('chuvApp.components.filters', [])
  .filter('highlight', function ($sce) {
    return function (text, phrase) {
      if (phrase) {
        text = text.replace(new RegExp('(' + phrase + ')', 'gi'), '<span class="highlighted">$1</span>')
      }
      return $sce.trustAsHtml(text)
    }
  });
