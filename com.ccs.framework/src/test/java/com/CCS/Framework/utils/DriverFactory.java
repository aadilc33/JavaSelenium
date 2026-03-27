package com.CCS.Framework.utils;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.time.Duration;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            WebDriver baseDriver = new ChromeDriver(options);

            WebDriverListeners1 listener = new WebDriverListeners1();
            WebDriver decorated = new EventFiringDecorator<>(listener).decorate(baseDriver);

            decorated.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            decorated.manage().window().maximize();
            driver.set(decorated);
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}