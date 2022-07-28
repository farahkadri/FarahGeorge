@Login
@SystemTesting
Feature: Login with Demo user

  Background: “George Ceska sporitelna App is downloaded
    Given Open “George Ceska sporitelna”. App

  @TestCaseKey=SID-T138
  Scenario: 1. Dashboard -Click on See All Events button
    When Click All Events button
    Then User should land on Events<test> tab
    Test Data
    1
    2


  @TestCaseKey=SID-T140
  Scenario: 2. Dashboard- Click on Global Next Event button
    When Click Global List Next Event
    Then Event Info Section static item is - "Status:"

  @TestCaseKey=SID-T515
  Scenario: 3. Dashboard - Click on See All Athletes
    When Click See All Athletes
    Then RankingList screen should be displayed

  @TestCaseKey=SID-T142
  Scenario: 4. Dashboard - Global Rankings Click next
    When Scroll to See All Athlete
    Then Click Go Right Arrow
    When Scroll to See All Athlete
    Then Click Go Left Arrow

  @TestCaseKey=SID-T139
  Scenario: 5. Dashboard - Click on See All Announcements button
    When Click All Announcements
    Then Announcements screen should be displayed "Announcements"

  @TestCaseKey=SID-T137
  Scenario: 6. Dashboard-Click on Dashboard Announcements
    When Click one of the Events on Announcements section
    Then Event Info Section static item is - "Status:"

  @TestCaseKey=SID-T135
  Scenario: 7. Dashboard - Add a Favorite Athlete
    When Scroll to See All Athlete
    Then Click Go Right Arrow
    Then Click index Global Ranking List Athlete
    Then Tap on Favorite Icon
    And Click Back button
    Then Click Go Left Arrow
    Then Heart icon is shown next to Athlete name

  @TestCaseKey=SID-T136
  Scenario Outline: 8. Add more than one Favorite Athlete
    When Open Side menu and click Search Athlete
    And Type Country "<Country>"
    And Click Country <index>
    And Type Athlete "<Athlete>"
    And Click on <index> Athlete on Search Athlete Page
    And Tap on Favorite Icon
    Then On Dashboard check Favourite Athlete "<Athlete>"
    Examples:
      | Country | index | Athlete  |
      | Italy   | 0     | SEMERARO |

  @TestCaseKey=SID-T149
  Scenario: 9. Verify the Favorite Athlete in Event List
    And Open Side menu and click Events
    Then Profile Events tab should display

  @TestCaseKey=SID-T143
  Scenario Outline: 10. Dashboard - Add Favorite Ranking in Global Ranking section
    When Click See All Athletes
    Then RankingList screen should be displayed
    And Mark the Category as favorite
    And Click Back button
    Then Open Side menu and click Rankings
    Then Click Rank <index> on Favourites section
    Examples:
      | index |
      | 0     |