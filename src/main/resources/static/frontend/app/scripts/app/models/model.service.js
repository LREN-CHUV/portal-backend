/**
 * Created by Michael DESIGAUD on 31/08/2015.
 */
'use strict';

angular.module('chuvApp.models')
    .factory('Model',['$resource','backendUrl','$http',function ($resource,backendUrl,$http) {

        var resource = $resource(backendUrl+'/models/:slug/:format',{slug:'@slug',format:'@format'},{
            update:{
                method:'PUT'
            },
            remove:{
                method: "DELETE"
            }
        });

        resource.executeQuery = function(data){
            return $http.post(backendUrl+"/queries/requests.json",data);
        };

        resource.getList = function(params){
            var params = {params:params};

            return $http.get(backendUrl+"/models",params);
        };

        resource.getSvg = function(slug){
            return $http.get(backendUrl+"/models/"+slug+".svg");
        };

        resource.copy = function(model){
            return $http.post(backendUrl+"/models/"+model.slug+"/copies",model);
        };

        return resource;

    }]);
