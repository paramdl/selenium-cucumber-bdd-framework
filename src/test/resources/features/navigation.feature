@navigation
Feature: Main navigation
  As an HR administrator
  I want every module reachable from the side menu
  So that I can move around the system quickly

  Background:
    Given I am logged in as the admin user

  @smoke
  Scenario: The side menu lists every module
    Then the side menu should list these modules in order:
      | Admin       |
      | PIM         |
      | Leave       |
      | Time        |
      | Recruitment |
      | My Info     |
      | Performance |
      | Dashboard   |
      | Directory   |
      | Maintenance |
      | Claim       |
      | Buzz        |

  @regression
  Scenario Outline: Each module opens from the side menu
    When I open the "<module>" module
    Then the URL should contain "<route>"
    And the page header should be "<header>"

    Examples:
      | module      | route         | header      |
      | Admin       | /admin/       | Admin       |
      | PIM         | /pim/         | PIM         |
      | Leave       | /leave/       | Leave       |
      | Time        | /time/        | Time        |
      | Recruitment | /recruitment/ | Recruitment |
      | Directory   | /directory/   | Directory   |
      | Claim       | /claim/       | Claim       |
      | Buzz        | /buzz/        | Buzz        |
