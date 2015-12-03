/**
 * Created by Florent PERINEL on 12/08/2015.
 */
'use strict';

angular.module('chuvApp.articles')
  .factory('Article',['$resource','backendUrl',function ($resource,backendUrl) {

    return $resource(backendUrl+'/articles/:slug/.json',{slug:'@slug'},{
        update:{
            method:'PUT'
        },
        remove:{
            method: "DELETE"
        }
    });

  }]);
