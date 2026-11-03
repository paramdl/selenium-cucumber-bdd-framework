package com.qa.stepdefinitions;

import com.qa.pages.AdminUsersPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdminUserSteps {
    private final AdminUsersPage usersPage = new AdminUsersPage();

    @Given("I am on the System Users page")
    public void iAmOnSystemUsers() {
        usersPage.open();
    }

    @When("I search for the username {string}")
    public void iSearchForUsername(String username) {
        usersPage.searchByUsername(username);
    }

    @When("I filter users by the role {string}")
    public void iFilterByRole(String role) {
        usersPage.filterByRole(role);
    }

    @Then("the result count should read {string}")
    public void theResultCountShouldRead(String text) {
        assertEquals(text, usersPage.getResultCountText());
    }

    @Then("every {string} in the results should be {string}")
    public void everyValueShouldBe(String column, String expected) {
        List<String> values = usersPage.getColumn(column);
        assertFalse("no rows returned", values.isEmpty());
        values.forEach(v -> assertEquals(column + " mismatch in " + values, expected, v));
    }

    @Then("I should see a {string} notification")
    public void iShouldSeeNotification(String text) {
        assertTrue("toast did not contain '" + text + "'", usersPage.getToastText().contains(text));
    }
}
