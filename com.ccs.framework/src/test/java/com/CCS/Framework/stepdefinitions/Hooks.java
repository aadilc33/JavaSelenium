package com.CCS.Framework.stepdefinitions;

import com.CCS.Framework.utils.DriverFactory;
import io.cucumber.java.*;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class Hooks {
    @Before
    public void beforeScenario() {
        // Initialize driver (already handled in DriverFactory if needed)
        DriverFactory.getDriver().get("https://www.crowncommercial.gov.uk/");
    }

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
        DriverFactory.quitDriver();
    }
}