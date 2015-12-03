var jasmineCucumber = require('protractor-jasmine-cucumber'),
    featureSteps = jasmineCucumber.featureSteps;

browser.ignoreSynchronization = true;

featureSteps('HBP homepage test')
    .given('I navigate to "(.*)"', function(url){
        browser.get(url, 2000).then(this.async());
    })
    .then('the title should be "(.*)"', function(contents){
        browser.getTitle()
            .then(function(title){
                expect(title).toContain(contents);
            })
            .then(this.async());
    });

featureSteps('My articles page')
    .given('I navigate to "(.*)"', function(url){
        browser.get(url, 2000).then(this.async());
    })
    .then('i click the "(.*)" link', function(contents){
        element(by.id('myArticles')).click().then(this.async);
    })
    .then('the url should contain "(.*)"', function(contents){
        browser.getLocationAbsUrl().then(function(url){
            expect(url)
                .toContain(contents);
        }).then(this.async());
    });

