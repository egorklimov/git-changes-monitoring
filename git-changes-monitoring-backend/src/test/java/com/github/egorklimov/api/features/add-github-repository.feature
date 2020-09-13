Feature: Add Github Repository

  Background:
    * def resourceUrl = apiBaseUrl + '/hello'

  Scenario: Call hello should return 200 status
    Given url resourceUrl
    When method GET
    Then status 200
    And match response == "hello"
