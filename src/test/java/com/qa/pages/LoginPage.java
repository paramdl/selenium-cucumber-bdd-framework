package com.qa.pages;

import com.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.util.List;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ALERT = By.cssSelector(".oxd-alert-content-text");
    private static final By FIELD_ERRORS = By.cssSelector(".oxd-input-field-error-message");

    /** The public demo occasionally answers 500 for a moment; reload once before failing. */
    public LoginPage open() {
        open("/auth/login");
        try {
            WaitUtil.waitForVisible(USERNAME);
        } catch (TimeoutException firstTry) {
            driver.navigate().refresh();
            WaitUtil.waitForVisible(USERNAME);
        }
        return this;
    }

    /** Opens any route relative to base.url, e.g. a protected page to check the redirect. */
    public void openPath(String path) {
        open(path);
    }

    public void loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public String getAlertText() {
        return getText(ALERT);
    }

    public List<String> getFieldErrors() {
        return getTexts(FIELD_ERRORS);
    }

    public boolean isLoaded() {
        WaitUtil.waitForUrl("/auth/login");
        return WaitUtil.waitForVisible(LOGIN_BUTTON).isDisplayed();
    }
}
