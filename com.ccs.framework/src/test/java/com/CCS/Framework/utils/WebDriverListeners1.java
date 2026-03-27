package com.CCS.Framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class WebDriverListeners1 implements WebDriverListener {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverListeners1.class);

    public void beforeGet(WebDriver driver, String url) {
        logger.info("Navigating to: {}", url);
    }
    public void afterGet(WebDriver driver, String url) {
        logger.info("Finished navigation to: {}", url);
    }
    public void beforeQuit(WebDriver driver) {
        logger.info("Closing browser.");
    }
    public void afterQuit(WebDriver driver) {
        logger.info("Browser closed.");
    }
    public void beforeClick(WebElement element) {
        logger.info("Attempting to click element: {}", describe(element));
    }
    public void afterClick(WebElement element) {
        logger.info("Clicked element: {}", describe(element));
    }
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        logger.info("Typing into element {}: {}", describe(element), String.join("", keysToSend));
    }
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        logger.info("Typed into element {}: {}", describe(element), String.join("", keysToSend));
    }

    public void beforeFindElement(WebDriver driver, By locator) {
        logger.info("Finding element by: {}", locator);
    }
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.info("Found element by: {}", locator);
    }

    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        logger.info("Executing script: {}", script);
    }
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        logger.info("Script executed. Result: {}", result);
    }

    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        logger.error("Error occurred in method: {} - Exception: {}", method.getName(), e.getCause(), e);
    }

    private String describe(WebElement element) {
        try {
            return element.toString();
        } catch (StaleElementReferenceException e) {
            return "Stale element"+element.toString();
        }
    }
}
