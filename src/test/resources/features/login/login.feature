@HU-02 @login
Feature: Access to PetStay
  As a pet owner
  I want to log in to PetStay with my username and password
  So that only I can manage my pets and their stays at the hotel

  @Issue-1 @REQ-ACC-01 @positive @smoketest
  Scenario Outline: Successful login with valid credentials
    Given I am on the PetStay login page
    When I log in with the username "<username>" and the password "<password>"
    Then I should be logged in as "<username>"
    Examples:
      | username | password     |
      | dueno    | huellitas123 |

  @Issue-2 @REQ-ACC-02 @negative
  Scenario Outline: Login is rejected with invalid credentials
    Given I am on the PetStay login page
    When I log in with the username "<username>" and the password "<password>"
    Then I should see the login error "Usuario o contraseña incorrectos"
    Examples:
      | username | password     |
      | dueno    | wrongpass    |
      | intruder | huellitas123 |
