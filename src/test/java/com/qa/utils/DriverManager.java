package com.qa.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * One WebDriver per thread so scenarios can run in parallel.
 * Drivers are resolved by Selenium Manager (built into Selenium 4), so no driver binaries are checked in.
 * Implicit wait stays at 0: every wait is explicit (see WaitUtil) to avoid the two compounding.
 */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver() {
        boolean headless = ConfigReader.isHeadless();
        // EAGER: continue once the DOM is ready instead of waiting for every image/script; waits handle the rest.
        WebDriver webDriver = switch (ConfigReader.getBrowser().toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) options.addArguments("--headless=new");
                yield new EdgeDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                options.addArguments("--disable-notifications", "--disable-popup-blocking");
                if (headless) options.addArguments("--headless=new");
                yield new ChromeDriver(options);
            }
        };
        webDriver.manage().timeouts().implicitlyWait(Duration.ZERO);
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        webDriver.manage().window().setSize(new Dimension(1920, 1080));
        DRIVER.set(webDriver);
    }

    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver != null) {
            webDriver.quit();
            DRIVER.remove();
        }
    }
}
