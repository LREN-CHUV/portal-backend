/**
 * Created by Florent PERINEL on 12/08/2015.
 */
'use strict';

angular.module('chuvApp.articles')

/**
 * Show and form articles controller
 */
  .controller('ArticleController', ['$scope', '$translatePartialLoader', '$translate', '$location', '$stateParams', 'Article', 'User', 'Model','backendUrl','ModalUtil',
    function ($scope, $translatePartialLoader, $translate, $location, $stateParams, Article, User, Model, backendUrl, ModalUtil) {

      /**
       * Initialize controller
       */
      $scope.init = function () {

        $translatePartialLoader.addPart('articles');
        $translatePartialLoader.addPart('requests');
        $translate.refresh();
        $scope.loadArticle();
        $scope.loadModels();
      };

      $scope.query = null;

      $scope.getLanguageCodeForTinyMCE = function () {
        if ($translate.use() === 'fr') {
          return "fr_FR";
        } else {
          return "en_GB";
        }
      };

      $scope.editorContentOption = {
        inline: false,
        toolbar: "undo redo | styleselect bold italic underline strikethrough | forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | eqneditor | link image",
        plugins: "advlist anchor autolink autosave bbcode charmap code colorpicker contextmenu " +
            "directionality emoticons fullpage fullscreen hr image insertdatetime layer legacyoutput link lists " +
            "importcss media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker " +
            "tabfocus table template textcolor textpattern visualblocks visualchars wordcount eqneditor",
        external_plugins: {
          "eqneditor": "/libs/tinymce/plugins/eqneditor/plugin.min.js"
        },
        language: $scope.getLanguageCodeForTinyMCE(),
        paste_data_images: true,
        language_url: "/libs/tinymce/langs/" + $scope.getLanguageCodeForTinyMCE() + ".js",
        height: 500
      };

      /**
       * Return true if is a creation action
       * @returns {boolean}
       */
      $scope.isNew = function () {
        return $stateParams.slug == null && $stateParams.action == null;
      };

      /**
       * Return the action code
       * @returns {boolean}
       */
      $scope.isReadOnly = function () {
        return $stateParams.action === 'show';
      };

      /**
       * Save article
       */
      $scope.save = function () {
        $scope.article.date = new Date().toISOString();
        $scope.publishArticle ? $scope.article.status = 'published' : $scope.article.status = 'closed';
        if ($scope.isNew()) {
          $scope.article = Article.save($scope.article,function(){
              $location.path("/home");
          });
        } else {
          $scope.article = Article.update($scope.article,function(){
              $location.path("/home");
          });
        }

      };

      /**
       * Load article object in controller
       */
      $scope.loadArticle = function () {
        if (!$scope.isNew()) {
          $scope.article = Article.get({slug:$stateParams.slug});
        } else {
          $scope.article = {
            status: "draft"
          };
        }
      };

      /**
       * Load models objects in controller
       */
      $scope.loadModels = function (params) {
        if(!params){
            params = {team:1};
        }
        $scope.models = Model.query(params);
      };

        /**
         * push pdf file to drop box
         * @param article
         */
      $scope.saveToDropbox = function(article){
          var options={
              // Success is called once all files have been successfully added to the user's
              // Dropbox, although they may not have synced to the user's devices yet.
              success: function () {
                  // Indicate to the user that the files have been saved.
                  alert("Success! Files saved to your Dropbox.");
              },
              // Error is called in the event of an unexpected response from the server
              // hosting the files, such as not being able to find a file. This callback is
              // also called if there is an error on Dropbox or if the user is over quota.
              error: function (errorMessage) {
                  alert(errorMessage);
              }
          };
          // local url not working for dev=> var baseUrl="http://chuv-backend.redfroggy.fr";
          var baseUrl=backendUrl;

          var slug = article.slug;
          // todo used apikey of current user
          Dropbox.save(baseUrl+"/articles/"+slug+".pdf?apikey=adminapikey", slug+".pdf", options);
      };

      $scope.showModal = function(article){
        ModalUtil.showModal($scope,article);
      };

      $scope.onChangeMine = function(checked){
          $scope.loadModels({team: checked ? 0 : 1,own:checked ? 1 : 0});
      };

      // Init
      $scope.init();
    }]);


angular.module('chuvApp.articles').controller('ArticleModalController', ['$scope','$modalInstance', '$modal', 'item', function ($scope,$modalInstance,$modal, item) {
        $scope.article = item.article;

        $scope.closeModal = function(){
            $modalInstance.dismiss('cancel');
        };
    }
]);

angular.module('chuvApp.articles').controller('ArticlesController', ['$scope','Article','ModalUtil','User',function ($scope,Article,ModalUtil,User) {
    $scope.articles = Article.query({team:1,own:0,status:'published',valid:1});

    $scope.showModal = function(article){
        ModalUtil.showModal($scope,article);
    };

    /**
     * Return true if object has been created by current user
     * @param obj
     * @returns {boolean}
     */
    $scope.isMine = function(obj) {
      return User.hasCurrent() ? obj.createdBy.id == User.current().id : false;
    };

    $scope.isAuthorized = function(article){
        return $scope.isMine(article);
    }
}
]);
