/**
 * Created by David JAY on 04/09/2015.
 */
'use strict';

angular.module('chuvApp.components.criteria')
    .factory('Group',['$resource','backendUrl',function ($resource,backendUrl) {
        var resource = $resource(backendUrl+'/groups',{},{});

        return resource;

    }]);
