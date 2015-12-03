/**
 * Created by Florent PERINEL on 12/08/2015.
 */
'use strict';

describe('Articles controllers tests', function () {

  // Load controller's modules
  beforeEach(module('chuvApp'));
  beforeEach(module('chuvApp.articles'));

  describe('ArticleController creation tests', function () {

      var ArticleController, scope, translate, stateparams, filter, httpBackend, location,_backendUrl;

      // Initialize the controller and a mock scope
      beforeEach(inject(function ($rootScope, $controller, $translate, $stateParams, $filter, $httpBackend, $location,backendUrl) {
          translate = $translate;
          stateparams = $stateParams;
          httpBackend = $httpBackend;
          filter = $filter;
          _backendUrl = backendUrl;
          scope = $rootScope.$new();
          location = $location;
          ArticleController = $controller('ArticleController', {$scope: scope});

          httpBackend.when('GET', 'i18n/en/common.json').respond(200);
          httpBackend.when('GET', 'i18n/fr/common.json').respond(200);
          httpBackend.when('GET', 'i18n/en/articles.json').respond(200);
          httpBackend.when('GET', 'i18n/fr/articles.json').respond(200);
          httpBackend.when('GET', 'i18n/en/requests.json').respond(200);
          httpBackend.when('GET', 'i18n/fr/requests.json').respond(200);
          httpBackend.when('GET', 'scripts/app/home/home.html').respond(200);
          httpBackend.when('GET', 'scripts/app/articles/articles-list.html').respond(200);
          httpBackend.when('GET', 'scripts/app/login/login.html').respond(200);

          var models = [];
          httpBackend.expectGET(_backendUrl+'/models').respond(models);

          httpBackend.flush();
      }));

      /**
       * After each unit test
       */
      afterEach(function () {
          httpBackend.verifyNoOutstandingExpectation();
          httpBackend.verifyNoOutstandingRequest();
      });

      it('Create new article', function () {
          expect(scope.isNew()).toBeTruthy();

          spyOn(location,"path");

          var article = {
              id: 1,
              title: 'title',
              slug: 'slug',
              content: 'content',
              abstract: 'abstract'
          };
          httpBackend.expectPOST(_backendUrl+'/articles.json').respond(article);

          scope.save();

          httpBackend.flush();

          expect(scope.article.id).toEqual(article.id);
          expect(scope.article.title).toEqual(article.title);
          expect(scope.article.content).toEqual(article.content);
          expect(location.path).toHaveBeenCalledWith("/home");
      });
  });

    describe('ArticleController edition tests', function () {

        var ArticleController, scope, stateParams, httpBackend, location, _backendUrl;

        var article = {
            id: 1,
            title: 'title',
            content: 'content',
            abstract: 'abstract'
        };

        // Initialize the controller and a mock scope
        beforeEach(inject(function ($rootScope, $controller, $httpBackend,$location,backendUrl) {
            stateParams = {slug:'my-title'};
            article.slug = stateParams.slug;
            httpBackend = $httpBackend;
            scope = $rootScope.$new();
            location = $location;
            _backendUrl = backendUrl;
            ArticleController = $controller('ArticleController', {$scope: scope,$stateParams:stateParams});

            httpBackend.when('GET', 'i18n/en/common.json').respond(200);
            httpBackend.when('GET', 'i18n/fr/common.json').respond(200);
            httpBackend.when('GET', 'i18n/en/articles.json').respond(200);
            httpBackend.when('GET', 'i18n/fr/articles.json').respond(200);
            httpBackend.when('GET', 'i18n/en/requests.json').respond(200);
            httpBackend.when('GET', 'i18n/fr/requests.json').respond(200);
            httpBackend.when('GET', 'scripts/app/home/home.html').respond(200);
            httpBackend.when('GET', 'scripts/app/articles/articles-list.html').respond(200);
            httpBackend.when('GET', 'scripts/app/login/login.html').respond(200);

            httpBackend.expectGET(_backendUrl+'/articles/'+stateParams.slug+'.json').respond(article);

            var models = [];
            httpBackend.expectGET(_backendUrl+'/models?team=1').respond(models);

            httpBackend.flush();
        }));

        it('Edit an article', function () {
            expect(scope.isNew()).toBeFalsy();

            spyOn(location,"path");

            article.content = "updatedContent";
            httpBackend.expectPUT(_backendUrl+'/articles/'+stateParams.slug+'.json').respond(article);

            scope.save();

            httpBackend.flush();

            expect(scope.article.id).toEqual(article.id);
            expect(scope.article.title).toEqual(article.title);
            expect(scope.article.content).toEqual(article.content);
            expect(location.path).toHaveBeenCalledWith("/home");
        });

        /**
         * After each unit test
         */
        afterEach(function () {
            httpBackend.verifyNoOutstandingExpectation();
            httpBackend.verifyNoOutstandingRequest();
        });
    });
});