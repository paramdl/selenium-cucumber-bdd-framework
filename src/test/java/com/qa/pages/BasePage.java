package com.qa.pages;

import com.qa.utils.ConfigReader;
import com.qa.utils.DriverManager;
import com.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Shared actions for every page. Locators are By objects resolved at call time, so nothing goes stale. */
public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected void open(String path) {
        driver.get(ConfigReader.getBaseUrl() + path);
    }

    protected void click(By locator) {
        WaitUtil.waitForClickable(locator).click();
    }

    /** Clears with select-all + delete: OrangeHRM's Vue inputs ignore WebElement.clear(). */
    protected void type(By locator, String text) {
        WebElement el = WaitUtil.waitForVisible(locator);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        if (text != null && !text.isEmpty()) el.sendKeys(text);
    }

    protected String getText(By locator) {
        return WaitUtil.waitForVisible(locator).getText().trim();
    }

    protected List<String> getTexts(By locator) {
        return WaitUtil.waitForAllVisible(locator).stream().map(e -> e.getText().trim()).toList();
    }

    protected boolean isDisplayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    /** OrangeHRM renders inputs without ids; this finds the input that belongs to a visible label. */
    protected static By inputByLabel(String label) {
        return By.xpath("//label[normalize-space()='" + label + "']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
