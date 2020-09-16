Feature: Add Github Repository

  Background:
    * def resourceUrl = apiBaseUrl + 'api/repository'

  Scenario: Call clone should add repository
    Given url resourceUrl + '/clone'
    And request { url: "https://github.com/egorklimov/git-changes-monitoring.git" }
    And header Accept = 'application/json'
    When method POST
    Then status 200
    And match response == { path: "cloned-repos/git-changes-monitoring/.git" }
