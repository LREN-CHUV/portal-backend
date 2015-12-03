/**
 * Created by David JAY on 07/09/2015.
 */
'use strict';

angular.module('chuvApp.components.criteria')
    .factory('Variable',['$resource','backendUrl',function ($resource,backendUrl) {
        var resource = $resource(backendUrl+'/variables',{},{});

        return resource;

    }]);
