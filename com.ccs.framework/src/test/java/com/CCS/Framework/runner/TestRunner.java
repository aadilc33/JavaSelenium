package com.CCS.Framework.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.CCS.Framework.stepdefinitions"},
        plugin = {
                "pretty",
                "json:target/cucumber.json",
                "com.CCS.Framework.utils.GetRunningTagListener",
                "html:target/cucumber-html-report.html",
        },
        monochrome = true
)
public class TestRunner {}