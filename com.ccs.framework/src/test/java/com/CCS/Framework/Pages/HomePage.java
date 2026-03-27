package com.CCS.Framework.Pages;

import com.CCS.Framework.utils.WaitUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;


public class HomePage {
    private final WebDriver driver;
    private final By searchInput= By.xpath("//form[contains(@action,'/agreements')]//input");
    private final By searchButton= By.xpath("//form[contains(@action,'/agreements')]//button");
    private final WaitUtils waitUtils;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        waitUtils = new WaitUtils(driver);
    }

    public void searchAgreement(String searchText) {
        waitUtils.sendKeysToElement(searchInput,searchText, Duration.ofMillis(2000));
        waitUtils.clickElement(searchButton, Duration.ofMillis(2000));
        //form[contains(@action,'/agreements')]//input, Digital
        //*[contains(text(),' Regulation ')]/ancestor::div[contains(@class,'govuk-form-group')]//*[contains(text(),'PCR2015')]
        //*[contains(text(),' Browse by category')]/ancestor::div[contains(@class,'govuk-form-group')]//*[contains(text(),'Technology')]
        //*[contains(text(),'Agreement ')]/ancestor::div[contains(@class,'govuk-form-group')]//*[contains(text(),'Dynamic Purchasing System')]
    }


    public void open() {

    }

    public void verifyThePage() {
        Assert.assertEquals("The title is mismatch","Crown Commercial Service - CCS", driver.getTitle());
    }
}