package com.qa.stepdef;

import com.qa.pages.Events.EventsInfoPage;
import com.qa.pages.Profile.ProfilePage;
import com.qa.pages.Profile.RankingResultsPage;
import com.qa.pages.Rankings.RankingCategoryPage;
import com.qa.pages.SearchAthlete.SearchAthletePage;
import com.qa.utils.GlobalParams;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LoginStepDef {

    @Then("Profile screen should be displayed \"([^\"]*)\"$")
    public void profileScreenShouldBeDisplayed(String Title) {
        Assert.assertEquals(new ProfilePage().getProfileTitle(), Title);
    }

    @When("^Tap on Favorite Icon$")
    public ProfilePage tapFavButton() throws Exception {
        new ProfilePage().tapFavButton();
        return new ProfilePage();
    }

    @When("^Click on Profile Rank$")
    public RankingCategoryPage clickProfileRank() throws Exception {
        new ProfilePage().clickProfileRank();
        return new RankingCategoryPage();
    }

    @When("^Click on (-?\\d+) City/Country$")
    public ProfilePage clickCities(int number) throws Exception {
        new ProfilePage().clickCities(number);
        return new ProfilePage();
    }

    @When("^Click on Last Event$")
    public ProfilePage clickLastEvent() throws Exception {
        new ProfilePage().clickLastEvent();
        return new ProfilePage();
    }

    @And("RankingList screen should be displayed$")
    public void RankingListScreenDisplayed() {
        switch (GlobalParams.getInstance().getPlatform()) {
            case "Android":
                String AndroidTitle = "World Karate Federation";
                Assert.assertEquals(new RankingCategoryPage().getTitle(), AndroidTitle);
                break;
            case "iOS":
                String iOSTitle = "Ranks 1-10";
                Assert.assertEquals(new RankingCategoryPage().getStaticItem(), iOSTitle);
                break;
        }

        //Assert.assertEquals(new RankingCategoryPage().getTitle(), Title);
    }

    @When("^Click on (-?\\d+) Ranking results$")
    public ProfilePage clickRankingResults(int index) throws Exception {
        new RankingResultsPage().clickRankingResults(index);
        return new ProfilePage();
    }

    @When("^Click on (-?\\d+) Profile Rank category$")
    public RankingCategoryPage clickProfileRankCategory(int index) throws Exception {
        new ProfilePage().clickProfileRankCategory(index);
        return new RankingCategoryPage();
    }

    @When("^Click on (-?\\d+) Athlete on Search Athlete Page$")
    public ProfilePage clickOnAthlete(int index) throws Exception {
        new SearchAthletePage().clickAthleteIndex(index);
        return new ProfilePage();
    }

    // @When("^Click on Ranking Results$")
    //public ProfilePage clickRankingResults() throws Exception {
    //  new RankingResults().clickRankingResults();
    //return new ProfilePage();
    //}

    @And("Ranking results alert screen should be displayed \"([^\"]*)\"$")
    public void alertScreenShouldBeDisplayed(String Title) {
        Assert.assertEquals(new RankingResultsPage().getAlertTitle(), Title);
    }

    @When("^Click on Open Event$")
    public EventsInfoPage openEvents() throws Exception {
        new RankingResultsPage().clickOpenEvent();
        return new EventsInfoPage();
    }

    @When("^Click on Open Ranking$")
    public RankingCategoryPage openRanking() throws Exception {
        new RankingResultsPage().clickOpenRanking();
        return new RankingCategoryPage();
    }

    @When("^Click on (-?\\d+) Open Profile Name and Compare$")
    public ProfilePage openProfile(int index) throws Exception {
        String athleteNameIndex = new ProfilePage().getAthleteNameWithIndex(index);
        new DashboardPage().selectAthlete(index);
        try {
            new TutorialOverlayPage().clickOKTutorialOverlay();
        } catch (Exception exception) {
            // Overlay not visible, no-op
        }
        String athleteProfileName = new ProfilePage().getAthleteName();
        Assert.assertTrue(athleteNameIndex.contains(athleteProfileName));
        return new ProfilePage();
    }

    @When("^Click on Show All Ranking results button$")
    public ProfilePage clickAllRankingResults() throws Exception {
        new RankingResultsPage().clickAllRankResultsBtn();
        return new ProfilePage();
    }

/*    @When("^Click on Show All Cities/Countries button$")
    public ProfilePage clickShowAllCitiesCountries() throws Exception {
        new ProfilePage().displayShowAllCitiesBtn();
        return new ProfilePage();
    }*/

    @When("^Click on Profile Rank dropdown$")
    public ProfilePage clickProfileRankDropdown() throws Exception {
        new ProfilePage().clickProfileRankDropdown();
        return new ProfilePage();
    }

    @When("^Click on Show All Cities/Countries button$")
    public ProfilePage clickShowAllCitiesCountries() throws Exception {
        new ProfilePage().clickShowAllCitiesButton();
        return new ProfilePage();
    }

    @When("^Click on Videos of Events$")
    public ProfilePage clickVideosOfEvents() throws Exception {
        new ProfilePage().clickVideosEvents();
        return new ProfilePage();
    }


    @And("Cities/Countries screen should be displayed \"([^\"]*)\"$")
    public void citiesScreenShouldBeDisplayed(String Title) {
        Assert.assertTrue(new ProfilePage().getTitle().equalsIgnoreCase(Title));
    }


    @And("^Type Ranking Results event \"([^\"]*)\"$")
    public void typeRankingResultsEvent(String event) {
        new ProfilePage().typeRankingResultsEvent(event);
    }

    @And("Click Search-Icon")
    public void clickSearchIcon() {
        new RankingResultsPage().clickSearchIcon();
    }
}
