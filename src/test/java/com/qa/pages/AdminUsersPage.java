package com.qa.pages;

import com.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Admin > User Management > System Users. */
public class AdminUsersPage extends BasePage {
    private static final By SEARCH = By.cssSelector("button[type='submit']");
    private static final By RESULT_COUNT = By.cssSelector(".orangehrm-horizontal-padding span.oxd-text");
    private static final By ROWS = By.cssSelector(".oxd-table-body .oxd-table-card");
    private static final By LOADER = By.cssSelector(".oxd-loading-spinner");
    private static final By TOAST = By.cssSelector(".oxd-toast");

    private static final Pattern COUNT_PATTERN = Pattern.compile("\\((\\d+)\\) Records? Found");
    private static final int PAGE_SIZE = 50;

    // Column positions in the results table (0 is the row checkbox).
    private static final int USERNAME_COL = 1;
    private static final int ROLE_COL = 2;

    public AdminUsersPage open() {
        open("/admin/viewSystemUsers");
        WaitUtil.waitForVisible(SEARCH);
        return this;
    }

    public void searchByUsername(String username) {
        type(inputByLabel("Username"), username);
        search();
    }

    public void filterByRole(String role) {
        click(By.xpath("//label[normalize-space()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]"));
        click(By.xpath("//div[@role='listbox']//span[normalize-space()='" + role + "']"));
        search();
    }

    /**
     * The table re-renders after a search. Waiting only for the spinner can read the OLD rows if the
     * spinner hasn't appeared yet, so wait for the previous first row to be detached first.
     */
    private void search() {
        WaitUtil.waitForVisible(ROWS);
        WebElement previousFirstRow = driver.findElements(ROWS).stream().findFirst().orElse(null);
        click(SEARCH);
        if (previousFirstRow != null) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.stalenessOf(previousFirstRow));
            } catch (TimeoutException ignored) {
                // same rows came back unchanged; fall through to the spinner check
            }
        }
        WaitUtil.waitForInvisibility(LOADER);
        waitForRowsToMatchCount();
    }

    /**
     * The count label and the rows render separately. Wait until the label shows a result and the table holds
     * that many rows (the page shows at most 50), so a fast reader never sees the empty in-between state.
     */
    private void waitForRowsToMatchCount() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    String label = d.findElement(RESULT_COUNT).getText().trim();
                    if (label.equals("No Records Found")) return true;
                    Matcher m = COUNT_PATTERN.matcher(label);
                    if (!m.find()) return false;
                    int expected = Math.min(Integer.parseInt(m.group(1)), PAGE_SIZE);
                    return d.findElements(ROWS).size() == expected;
                });
    }

    public String getResultCountText() {
        return getText(RESULT_COUNT);
    }

    public String getToastText() {
        return getText(TOAST);
    }

    public List<String> getColumn(String column) {
        int index = switch (column) {
            case "Username" -> USERNAME_COL;
            case "User Role" -> ROLE_COL;
            default -> throw new IllegalArgumentException("Unknown column: " + column);
        };
        // Retry the whole read if a row is replaced mid-read.
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElements(ROWS).stream()
                        .map(row -> row.findElements(By.cssSelector(".oxd-table-cell")))
                        .map(cells -> cells.get(index))
                        .map(WebElement::getText)
                        .map(String::trim)
                        .toList());
    }
}
