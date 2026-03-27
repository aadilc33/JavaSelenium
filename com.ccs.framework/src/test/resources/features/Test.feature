Feature: Login Functionality
  As a user
  I want to be able to log in to the system
  So that I can access my account

  Background:
    Given I am on homepage
    And I search the agreement as "Digital"

  Scenario: Search and validate 5 digital agreements
    Given I apply the following filter:
      | Category           | Select                    |
      | Regulation        | PCR2015                   |
      | Agreement type     | Dynamic Purchasing System |
      | Browse by category | Technology                |
    Then I should see exactly 5 documents

#  Scenario: Navigate and validate key facts of each agreement
#    Given I apply the following filter:
#      | Category           | Select                    |
#      | Regulations        | PCR2015                   |
#      | Agreement Type     | Dynamic Purchasing System |
#      | Browse by Category | Technology                |
#    When I navigate to each agreement document
#    Then I verify the key facts section
#    And I validate the agreement details "(heading, ID, start date)" for all 5 documents
