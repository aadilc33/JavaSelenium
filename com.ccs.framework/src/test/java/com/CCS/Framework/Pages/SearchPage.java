package com.CCS.Framework.Pages;

import com.CCS.Framework.utils.WaitUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class SearchPage {
    private final WaitUtils waitUtils;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        waitUtils= new WaitUtils(driver);
    }

    public void applyFilterToTheResultPage(List<Map<String, String>> searchFilter) {
        for (Map<String, String> filter : searchFilter) {
            String searchCriteria = "//*[contains(text(),'%s')]/ancestor::div[contains(@class,'govuk-form-group')]//*[contains(text(),'%s')]";
            String locator =String.format(searchCriteria,filter.get("Category"),filter.get("Select"));
            waitUtils.webDriverWaitAndClickXPath(locator, Duration.ofMillis(2000));
        }
    }

    public void verifyTheNoOfDocuments(int arg0) {
        String noOfAgreement = ("//ul[@class='govuk-list govuk-list--frameworks']/li");
        waitUtils.isMoreThan1ElementsPresentByXPath(noOfAgreement,Duration.ofMillis(2000));
        Assert.assertTrue(waitUtils.webDriverWaitForElementsByXPath(noOfAgreement,Duration.ofMillis(2000)).size()>=arg0);
    }
}