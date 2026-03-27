package com.CCS.Framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class WaitUtils {

    private final WebDriver driver;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    // ================== FLUENT WAIT METHODS ================== //

    public WebElement fluentWaitForElementVisible(By locator, Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement fluentWaitForElementClickable(By locator, Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(ElementNotInteractableException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean fluentWaitForTitleContains(String titlePart, Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(500))
                .until(ExpectedConditions.titleContains(titlePart));
    }

    public <T> T fluentWaitForCustomCondition(Function<WebDriver, T> condition, Duration timeout, Class<? extends Throwable>... ignoreExceptions) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(500));

        if (ignoreExceptions != null && ignoreExceptions.length > 0) {
            wait.ignoreAll(Arrays.asList(ignoreExceptions));
        }

        return wait.until(condition);
    }

    // ================== WEBDRIVER WAIT METHODS ================== //

    public WebElement webDriverWaitForElementVisible(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement webDriverWaitForElementClickable(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean webDriverWaitForTitleContains(String titlePart, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.titleContains(titlePart));
    }

    public <T> T webDriverWaitForCustomCondition(Function<WebDriver, T> condition, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(condition);
    }

    public void sendKeysToElement(By locator, String inputText, Duration timeout) {
        WebElement element = fluentWaitForElementVisible(locator, timeout);
        element.clear(); // Optional: clear existing text before typing
        element.sendKeys(inputText);
    }

    public void clickElement(By locator, Duration timeout) {
        WebElement element = fluentWaitForElementClickable(locator, timeout);
        element.click();
    }

    public WebElement webDriverWaitForXPathVisible(String xpath, Duration timeout) {
        By locator = By.xpath(xpath);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement webDriverWaitForXPathClickable(String xpath, Duration timeout) {
        By locator = By.xpath(xpath);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void webDriverWaitAndClickXPath(String xpath, Duration timeout) {
        By locator = By.xpath(xpath);
        WebElement element = new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }
    public boolean isMoreThan1ElementsPresentByXPath(String xpath, Duration timeout) {
        By locator = By.xpath(xpath);
        List<WebElement> elements = new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return !elements.isEmpty();
    }
    public List<WebElement> webDriverWaitForElementsByXPath(String xpath, Duration timeout) {
        By locator = By.xpath(xpath);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

}