/**
 * Created by Michael DESIGAUD on 11/09/2015.
 */

'use strict';

angular.module('chuvApp.components.widget').factory('WidgetService', ['$window', function($window) {
    $window.$.wijets.registerAction( {
        handle: "refresh-demo",
        html: '<span class="button-icon"><i class="ti ti-reload"></i></span>',
        onClick: function () {
            var params = $(this).data('actionParameters');
            var widget = $(this).closest('[data-widget]');
            widget.append('<div class="panel-loading"><div class="panel-loader-' + params.type + '"></div></div>');
            setTimeout( function () {
                widget.find('.panel-loading').remove();
            }, 2000);
        }
    });
    return $window.$.wijets;
}]);