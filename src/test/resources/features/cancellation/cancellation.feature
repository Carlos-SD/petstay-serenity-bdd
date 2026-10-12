@HU-02 @cancellation
Feature: My reservations and cancellation
  As a pet owner
  I want to cancel the reservations I no longer need
  So that I get back the money that corresponds to me

  Background:
    Given I am logged in to PetStay
    And I have registered the pet "Max", a "Perro" of "8" kg and "3" years old

  @Issue-14 @REQ-CAN-03 @REQ-CAN-04 @positive
  Scenario Outline: The refund depends on the days left before the entry date
    Given I have booked a stay for "Max" entering in <entry> days and leaving in <exit> days
    When I cancel that reservation
    Then that reservation should have status "Cancelada" and refund "<refund>"
    Examples:
      | entry | exit | refund  |
      | 3     | 5    | $80.000 |
      | 1     | 3    | $40.000 |
      | 0     | 2    | $0      |

  @Issue-15 @REQ-CAN-02 @positive
  Scenario: Cancelling asks for confirmation before doing it
    Given I have booked a stay for "Max" entering in 4 days and leaving in 6 days
    When I start cancelling that reservation but go back
    Then that reservation should still have status "Confirmada"

  @Issue-16 @REQ-CAN-04 @negative
  Scenario: A cancelled reservation cannot be cancelled again
    Given I have booked a stay for "Max" entering in 4 days and leaving in 6 days
    When I cancel that reservation
    Then that reservation should not offer the option to cancel it again
