package com.CCS.Framework.stepdefinitions;

import com.CCS.Framework.Pages.SearchPage;
import com.CCS.Framework.utils.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;


public class SearchPageSteps {

    private final SearchPage searchPage;

    public SearchPageSteps()
    {
        WebDriver driver = DriverFactory.getDriver();
        searchPage =new SearchPage(driver);
    }



    @Given("I apply the following filter:")
    public void iApplyTheFollowingFilter(DataTable dataTable) {
        List<Map<String, String>> searchFilter = dataTable.asMaps();
        searchPage.applyFilterToTheResultPage(searchFilter);
    }

    @Then("I should see exactly {int} documents")
    public void iShouldSeeExactlyDocuments(int arg0) {
        searchPage.verifyTheNoOfDocuments(arg0);
    }

    @When("I navigate to each agreement document")
    public void iNavigateToEachAgreementDocument() {
    }

    @Then("I verify the key facts section")
    public void iVerifyTheKeyFactsSection() {
    }
}