/**
 * Created by Florent PERINEL on 14/08/2015.
 */
'use strict';

angular.module('chuvApp.users')
  .factory('User', ['$rootScope','backendUrl','$http','$cookieStore','base64', function ($rootScope,backendUrl,$http,$cookieStore,base64) {

    var currentUser;

    return {

      current: function () {
        return $cookieStore.get('user');
      },
      hasCurrent : function(){
          return $cookieStore.get('user') !== null && $cookieStore.get('user') !== undefined;
      },
      removeCurrent: function () {
            return $cookieStore.remove('user');
      },

      authenticate: function (login, password) {
          currentUser = {username:login,password:password};
          var cString = login + ':' + password;
          return $http.post(backendUrl+"/authenticate",currentUser,{
              headers:{
                'Authorization':'Basic ' + base64.encode(cString)
              }
          }).success(function (response) {
              $cookieStore.put('user',response);
              return response;
           }
          );
      },

      get: function (id) {
        $http.get("/users/"+id).success(function(data) {
          return data;
        }).error(function() {
          return null;
        });
      },

      create: function (user) {
        return user
      },

      update: function (user) {
        return user
      },

      remove: function (id) {
        for (var idx in datas) {
          if (datas.hasOwnProperty(idx) && datas[idx].id == id) {
            datas.splice(idx, 1);
            return;
          }
        }
      }

    };

  }]);
