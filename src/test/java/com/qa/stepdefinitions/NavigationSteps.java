package com.qa.stepdefinitions;

import com.qa.pages.DashboardPage;
import com.qa.utils.WaitUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NavigationSteps {
    private final DashboardPage dashboard = new DashboardPage();

    @Then("the side menu should list these modules in order:")
    public void theSideMenuShouldList(DataTable table) {
        List<String> expected = table.asList();
        assertEquals(expected, dashboard.getMenuItems());
    }

    @When("I open the {string} module")
    public void iOpenTheModule(String module) {
        dashboard.openModule(module);
    }

    @Then("the URL should contain {string}")
    public void theUrlShouldContain(String fragment) {
        assertTrue("URL did not reach " + fragment + ": " + dashboard.getCurrentUrl(), WaitUtil.waitForUrl(fragment));
    }

    @Then("the page header should be {string}")
    public void thePageHeaderShouldBe(String header) {
        assertEquals(header, dashboard.getHeader());
    }
}
