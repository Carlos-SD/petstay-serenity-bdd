@HU-02 @booking
Feature: Stay booking
  As a pet owner
  I want to book stays with additional services for my pets
  So that they are well cared for while I am away

  Dates are written as days from today, so the scenarios keep working on any execution date.

  Background:
    Given I am logged in to PetStay

  @Issue-8 @REQ-RES-08 @REQ-RES-10 @positive @smoketest
  Scenario Outline: Book a stay and receive a confirmation code
    Given I have registered the pet "<pet>", a "<species>" of "<weight>" kg and "<age>" years old
    When I book a stay for "<pet>" entering in <entry> days and leaving in <exit> days
    Then I should receive a reservation code with the format PS-XXXX
    And my reservation for "<pet>" should be listed with status "Confirmada" and total "<total>"
    Examples:
      | pet | species | weight | age | entry | exit | total    |
      | Max | Perro   | 8      | 3   | 2     | 5    | $120.000 |

  @Issue-9 @REQ-RES-05 @REQ-RES-07 @positive
  Scenario Outline: Stays of more than 7 nights get a 15% discount on lodging only
    Given I have registered the pet "Max", a "Perro" of "<weight>" kg and "3" years old
    When I request the total of a stay for "Max" entering in 2 days and leaving in <exit> days
    Then the summary should show <nights> nights, lodging "<lodging>", discount "<discount>" and total "<total>"
    Examples:
      | weight | exit | nights | lodging  | discount  | total    |
      | 8      | 10   | 8      | $320.000 | -$48.000  | $272.000 |
      | 30     | 12   | 10     | $700.000 | -$105.000 | $595.000 |

  @Issue-10 @REQ-RES-06 @negative
  Scenario: The daily walk is not offered for cats
    Given I have registered the pet "Michi", a "Gato" of "4" kg and "2" years old
    When I start a booking for "Michi"
    Then the daily walk should not be available
    And I should see the booking note "El paseo solo está disponible para perros."

  @Issue-11 @REQ-RES-03 @REQ-RES-04 @negative
  Scenario Outline: A stay with invalid dates is rejected
    Given I have registered the pet "Max", a "Perro" of "8" kg and "3" years old
    When I request the total of a stay for "Max" entering in <entry> days and leaving in <exit> days
    Then I should see the error "<error>" in the "<field>" date field
    And no summary should be shown
    Examples:
      | entry | exit | field | error                                                 |
      | -1    | 2    | entry | La fecha de entrada no puede ser anterior a hoy       |
      | 3     | 3    | exit  | La fecha de salida debe ser posterior a la de entrada |
      | 1     | 32   | exit  | La estadía máxima es de 30 noches                     |

  @Issue-12 @REQ-RES-09 @negative
  Scenario: A pet cannot have two active reservations with overlapping dates
    Given I have registered the pet "Max", a "Perro" of "8" kg and "3" years old
    And I have booked a stay for "Max" entering in 5 days and leaving in 10 days
    When I request the total of a stay for "Max" entering in 8 days and leaving in 12 days
    Then I should see the booking message "Esta mascota ya tiene una reserva activa en esas fechas"
    And no summary should be shown

  @Issue-13 @REQ-RES-09 @positive
  Scenario: A new stay can start on the same day the previous one ends
    Given I have registered the pet "Max", a "Perro" of "8" kg and "3" years old
    And I have booked a stay for "Max" entering in 5 days and leaving in 10 days
    When I book a stay for "Max" entering in 10 days and leaving in 12 days
    Then I should receive a reservation code with the format PS-XXXX
    And I should have 2 reservations with status "Confirmada"

  @Issue-17 @REQ-RES-07 @negative @defect
  Scenario: A stay of exactly 7 nights does not get the long-stay discount
    Given I have registered the pet "Max", a "Perro" of "8" kg and "3" years old
    When I request the total of a stay for "Max" entering in 2 days and leaving in 9 days
    Then the summary should show 7 nights, lodging "$280.000", no discount and total "$280.000"

  @Issue-18 @REQ-RES-06 @positive @defect
  Scenario: The daily walk is charged for every night of the stay
    Given I have registered the pet "Max", a "Perro" of "8" kg and "3" years old
    When I request the total of a stay for "Max" entering in 2 days and leaving in 5 days, with bath and daily walk
    Then the summary should show bath "$25.000", daily walk "$30.000" and total "$175.000"
