@userLoginModule
Feature: Testing User Flow dietician

  Scenario: Check user able to login
    Given Passing "LoginSheet" and Fillo API is up and running
    When User send POST HTTP request with endpoint
    Then User validates the response body 
