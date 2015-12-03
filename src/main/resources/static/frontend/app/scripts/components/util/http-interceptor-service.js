/**
 * Created by Michael DESIGAUD on 26/08/2015.
 */
/**
 * Http interceptor to add proper headers to the request.
 */
angular.module('chuvApp.util').factory('httpSecurityInterceptor', ['$cookieStore', 'base64', 'backendUrl', function ($cookieStore, base64, backendUrl) {
        return {
            /**
             * Request interceptor
             * @param request http request original
             * @returns {*} http request modified
             */
            request: function (request) {
                //Public urls are ignored
                if (request.url.indexOf(backendUrl) === -1) {
                    return request;
                }
                var user = $cookieStore.get('user');
                if(user) {
                    var cString = user.username + ':' + user.password;
                    request.headers['Authorization'] = 'Basic ' + base64.encode(cString);
                }
                return request;
            }
        };
    }]
);
