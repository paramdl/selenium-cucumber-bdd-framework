@admin
Feature: System user search
  As an HR administrator
  I want to find system users by name and role
  So that I can review who has access

  # The public demo is shared, so these checks assert search behaviour, not data other visitors can edit.

  Background:
    Given I am logged in as the admin user
    And I am on the System Users page

  @smoke
  Scenario: Searching for an existing username returns exactly that user
    When I search for the username "Admin"
    Then the result count should read "(1) Record Found"
    And every "Username" in the results should be "Admin"

  @regression
  Scenario: Searching for an unknown username returns nothing
    When I search for the username "no_such_user_zz9"
    Then I should see a "No Records Found" notification
    And the result count should read "No Records Found"

  @regression
  Scenario Outline: Filtering by role only returns users with that role
    When I filter users by the role "<role>"
    Then every "User Role" in the results should be "<role>"

    Examples:
      | role |
      | Admin |
      | ESS   |
