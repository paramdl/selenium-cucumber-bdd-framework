package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {
    private static final Logger LOG = LogManager.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {}

    public static byte[] captureAsBytes() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /** Saves a PNG next to the reports and returns its path, or null if capture failed. */
    public static Path captureAndSave(String scenarioName) {
        try {
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path dir = Path.of(ConfigReader.getScreenshotsPath());
            Files.createDirectories(dir);
            Path file = dir.resolve(scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + stamp + ".png");
            Files.write(file, captureAsBytes());
            LOG.info("Screenshot saved: {}", file.toAbsolutePath());
            return file;
        } catch (IOException e) {
            LOG.error("Failed to save screenshot: {}", e.getMessage());
            return null;
        }
    }
}
