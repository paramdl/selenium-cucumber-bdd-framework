package com.qa.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// Tags come from -Dcucumber.filter.tags (default "not @skip", set in pom.xml).
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.stepdefinitions", "com.qa.hooks"},
        plugin = {
                "pretty",
                "html:test-output/cucumber-report.html",
                "json:test-output/cucumber-report.json",
                "junit:test-output/cucumber-report.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class TestRunner {
}
