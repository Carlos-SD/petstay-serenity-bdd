@HU-02 @pets
Feature: Pet registration
  As a pet owner
  I want to register my pets at PetStay
  So that I can book stays for them

  Background:
    Given I am logged in to PetStay

  @Issue-3 @REQ-PET-01 @REQ-PET-05 @positive
  Scenario Outline: Register a pet with valid data
    When I register the pet "<name>", a "<species>" of "<weight>" kg and "<age>" years old, with vaccines up to date
    Then I should see the pets message "<name> fue registrado con tamaño <size>"
    And the pet "<name>" should be listed with species "<species>", weight "<listedWeight>", age "<listedAge>" and size "<size>"
    Examples:
      | name  | species | weight | age | listedWeight | listedAge | size    |
      | Max   | Perro   | 12.5   | 3   | 12,5 kg      | 3 años    | Mediano |
      | Michi | Gato    | 4      | 1   | 4 kg         | 1 año     | Pequeño |

  @Issue-4 @REQ-PET-03 @positive
  Scenario Outline: The size is assigned automatically from the weight
    When I register the pet "<name>", a "Perro" of "<weight>" kg and "3" years old, with vaccines up to date
    Then the pet "<name>" should be listed with size "<size>"
    Examples:
      | name  | weight | size    |
      | Bruno | 10     | Pequeño |
      | Toby  | 10.1   | Mediano |
      | Rocky | 24.9   | Mediano |
      | Zeus  | 25     | Grande  |

  @Issue-5 @REQ-PET-02 @negative
  Scenario Outline: A pet without vaccines up to date cannot be registered
    When I register the pet "<name>", a "<species>" of "<weight>" kg and "<age>" years old, without vaccines up to date
    Then I should see the pets message "No podemos hospedar mascotas sin vacunas al día"
    And my pets list should have 0 pets
    Examples:
      | name  | species | weight | age |
      | Lucas | Perro   | 15     | 4   |

  @Issue-6 @REQ-PET-04 @negative
  Scenario Outline: Two pets cannot share the same name, regardless of upper or lower case
    Given I have registered the pet "<name>", a "Perro" of "12" kg and "3" years old
    When I register the pet "<duplicatedName>", a "Gato" of "4" kg and "2" years old, with vaccines up to date
    Then I should see the error "Ya existe una mascota registrada con ese nombre" in the pet "name" field
    And my pets list should have 1 pet
    Examples:
      | name | duplicatedName |
      | Luna | LUNA           |
      | Luna | luna           |

  @Issue-7 @REQ-PET-01 @negative
  Scenario Outline: Pet data outside the allowed ranges is rejected
    When I register the pet "<name>", a "Perro" of "<weight>" kg and "<age>" years old, with vaccines up to date
    Then I should see the error "<error>" in the pet "<field>" field
    And my pets list should have 0 pets
    Examples:
      | name                            | weight | age | field  | error                                        |
      | L                               | 12     | 3   | name   | El nombre debe tener entre 2 y 30 caracteres |
      | Maximiliano Alejandro Rodriguez | 12     | 3   | name   | El nombre debe tener entre 2 y 30 caracteres |
      | Max                             | 0      | 3   | weight | El peso debe ser mayor que 0 y máximo 80 kg  |
      | Max                             | 80.5   | 3   | weight | El peso debe ser mayor que 0 y máximo 80 kg  |
      | Max                             | 12     | 26  | age    | La edad debe estar entre 0 y 25 años         |
