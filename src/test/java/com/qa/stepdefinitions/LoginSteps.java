package com.qa.stepdefinitions;

import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import com.qa.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginSteps {
    private final LoginPage loginPage = new LoginPage();
    private final DashboardPage dashboard = new DashboardPage();

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage.open();
    }

    @Given("I am logged in as the admin user")
    public void iAmLoggedInAsAdmin() {
        loginPage.open();
        loginPage.loginAs(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        assertTrue("dashboard did not load after login", dashboard.isLoaded());
    }

    @When("I log in with valid admin credentials")
    public void iLogInWithValidAdminCredentials() {
        loginPage.loginAs(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWith(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @When("I submit the login form without credentials")
    public void iSubmitEmpty() {
        loginPage.loginAs("", "");
    }

    @When("I log out")
    public void iLogOut() {
        dashboard.logout();
    }

    @When("I open {string} without logging in")
    public void iOpenWithoutLoggingIn(String path) {
        loginPage.openPath(path);
    }

    @Then("I should see the dashboard")
    public void iShouldSeeTheDashboard() {
        assertTrue("dashboard did not load", dashboard.isLoaded());
    }

    @Then("I should see the login error {string}")
    public void iShouldSeeTheLoginError(String message) {
        assertEquals(message, loginPage.getAlertText());
    }

    @Then("both fields should show {string}")
    public void bothFieldsShouldShow(String message) {
        assertEquals(Collections.nCopies(2, message), loginPage.getFieldErrors());
    }

    @Then("I should be on the login page")
    public void iShouldBeOnTheLoginPage() {
        assertTrue("login page not shown", loginPage.isLoaded());
    }
}
