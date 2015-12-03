Feature: My articles
  Scenario: Test My articles link
    Given I navigate to url http://127.0.0.1:8191
    Then i click the My articles link
    Then the url should contain /articles