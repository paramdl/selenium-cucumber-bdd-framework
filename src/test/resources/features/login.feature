@login
Feature: Login
  As an HR administrator
  I want to sign in securely
  So that only authorised people reach employee data

  Background:
    Given I am on the login page

  @smoke
  Scenario: Admin signs in with valid credentials
    When I log in with valid admin credentials
    Then I should see the dashboard

  @regression
  Scenario Outline: Invalid credentials are rejected with one generic message
    When I log in with username "<username>" and password "<password>"
    Then I should see the login error "Invalid credentials"

    Examples:
      | case             | username         | password      |
      | wrong password   | Admin            | not-the-pass  |
      | unknown user     | no_such_user_zz9 | admin123      |
      | wrong case user  | ADMIN-x          | admin123      |

  @regression
  Scenario: Both fields are required
    When I submit the login form without credentials
    Then both fields should show "Required"

  @regression
  Scenario: Logging out returns to the login page
    When I log in with valid admin credentials
    And I log out
    Then I should be on the login page

  @regression @security
  Scenario: Protected pages redirect to login when signed out
    When I open "/admin/viewSystemUsers" without logging in
    Then I should be on the login page
