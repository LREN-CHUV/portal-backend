/**
 * Created by Michael DESIGAUD on 10/09/2015.
 */
angular.module('chuvApp.util').factory('ModalUtil',['$modal',function($modal) {
    return{
        showModal:function (scope,article,okCallback) {

            scope.opts = {
                backdrop: true,
                backdropClick: true,
                dialogFade: false,
                keyboard: true,
                controller: 'ArticleModalController',
                templateUrl: 'scripts/app/articles/article-modal.html',
                resolve: {}
            };

            scope.opts.resolve.item = function () {
                // pass resident to Dialog
                return angular.copy({article:article,okCallback:okCallback});
            };
            $modal.open(scope.opts);
        }
    }
}]);