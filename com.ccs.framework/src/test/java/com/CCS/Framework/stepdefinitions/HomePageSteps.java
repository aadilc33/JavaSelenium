package com.CCS.Framework.stepdefinitions;

import com.CCS.Framework.Pages.HomePage;
import com.CCS.Framework.utils.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HomePageSteps {

    public final HomePage homePage;
    private static final Logger logger = LoggerFactory.getLogger(HomePageSteps.class);

    public HomePageSteps() {
        WebDriver driver = DriverFactory.getDriver();
        homePage = new HomePage(driver);
        logger.info("initialized the HomePage");
    }


    @Given("I am on homepage")
    public void iAmOnHomepage() {
        homePage.verifyThePage();
    }


    @And("I search the agreement as {string}")
    public void iSearchTheAgreementAs(String arg0) {
        homePage.searchAgreement(arg0);
    }

}