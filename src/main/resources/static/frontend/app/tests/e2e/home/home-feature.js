/**
 * Created by Michael DESIGAUD on 20/08/2015.
 */
var jasmineCucumber = require('protractor-jasmine-cucumber'),
    feature = jasmineCucumber.feature;

feature('HBP homepage test')
    .scenario('HBP homepage test')
    .given('I navigate to "http://127.0.0.1:8191"')
    .then('the title should be "Brain Project Portal"');

feature('My articles page')
    .scenario('My articles page')
    .given('I navigate to "http://127.0.0.1:8191"')
    .then('i click the "My articles" link')
    .then('the url should contain "/articles"');