package com.qa.hooks;

import com.qa.utils.ConfigReader;
import com.qa.utils.DriverManager;
import com.qa.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {
    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        LOG.info("=== Starting: {} ===", scenario.getName());
        DriverManager.initDriver();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && ConfigReader.screenshotOnFailure()) {
                scenario.attach(ScreenshotUtil.captureAsBytes(), "image/png", "Failure screenshot");
                ScreenshotUtil.captureAndSave(scenario.getName());
            }
            LOG.info("=== {}: {} ===", scenario.getStatus(), scenario.getName());
        } finally {
            DriverManager.quitDriver();
        }
    }
}
