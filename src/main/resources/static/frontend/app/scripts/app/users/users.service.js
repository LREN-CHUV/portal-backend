/**
 * Created by Florent PERINEL on 14/08/2015.
 */
'use strict';

angular.module('chuvApp.users')
  .factory('User', ['$rootScope','backendUrl','$http','$cookieStore','base64', function ($rootScope,backendUrl,$http,$cookieStore,base64) {

    var datas = [
      { id: 1, login: "admin", password: "kitten", firstname: "Bogdan", lastname: "DRAGANSKI", fullname: "Bogdan DRAGANSKI", team: "Scientist",
        picture: "images/users/Bogdan.jpg",  web:"http://www.hbpproject.eu", email: "admin@hbpproject.eu", phone:"+33 6 00 00 00 00", birthday: "1982-01-22", gender:"Male",
        languages:["French"], city:"Lausanne", country:"Switzerland" },

      { id: 2, login: "l.dib", password: "user", firstname: "Linda", lastname: "DIB", fullname: "Linda DIB", team: "Scientist",
        picture: "images/users/Linda.png", web:"http://www.hbpproject.eu",
        email: "a.kiliaan@hbpproject.eu", phone:"+33 6 00 00 00 00", birthday: "1983-05-02", gender:"Female",
        languages:["French","Netherlands"], city:"Nijmegen", country:"Netherlands" },

      { id: 3, login: "llaursen", password: "poc", firstname: "Lucas", lastname: "LAURSEN", fullname: "Lucas LAURSEN", team: "Press",
        picture: "images/users/llaursen.jpg", web:"http://www.hbpproject.eu", email: "llaursen@hbpproject.eu", phone:"06 00 00 00 00", birthday: "1982-05-10", gender:"Male",
        languages:["French","Spanish"], city:"Madric", country:"Spain"  }
    ];

    var currentUser = datas[0];

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
        for (var idx in datas) {
          if (datas.hasOwnProperty(idx) && datas[idx].id == id) {
            return datas[idx];
          }
        }
        return null;
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
