Feature: Perfecto Appium Test with Cucumber

  Scenario: Launch the app and perform actions
    Given I launch the app on a Perfecto device
    When I perform an action
    Then the action should be successful