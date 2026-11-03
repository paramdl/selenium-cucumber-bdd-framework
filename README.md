# Selenium + Cucumber BDD Framework (Java)

[![tests](https://github.com/paramdl/selenium-cucumber-bdd-framework/actions/workflows/tests.yml/badge.svg)](https://github.com/paramdl/selenium-cucumber-bdd-framework/actions/workflows/tests.yml)

A Java 17 · Selenium 4 · Cucumber 7 framework for web UI testing, written in plain-English Gherkin and
demonstrated against the public [OrangeHRM demo](https://opensource-demo.orangehrmlive.com) (an HR admin app).

| Feature file | Scenarios | Covers |
|---|---|---|
| [`login.feature`](src/test/resources/features/login.feature) | 7 | Valid login, invalid credentials (data-driven), required fields, logout, protected-page redirect |
| [`navigation.feature`](src/test/resources/features/navigation.feature) | 9 | Side menu contents and order, every module opens to the right route and header |
| [`admin_users.feature`](src/test/resources/features/admin_users.feature) | 4 | User search by name, empty results, role filter returns only that role |

## Design
- **Page Object Model** with `By` locators resolved at call time (no stale PageFactory proxies) and a shared `BasePage`.
- **Explicit waits only** (`WaitUtil`); implicit wait is 0 so the two never compound into slow, flaky tests.
- **Thread-safe driver** (`ThreadLocal`) created and closed per scenario in Cucumber hooks; **Selenium Manager** resolves drivers, so no binaries or WebDriverManager.
- **Eager page loads** so slow third-party assets don't time out a test that only needs the DOM.
- **Shared-data aware:** the demo is used by many people, so assertions check behaviour (counts, roles, routes), not records others can edit.
- **Race-safe tables:** searches wait for the old rows to detach before reading results.
- **Config** in `config.properties`, overridable by `-Dkey=value` or environment variables (`BASE_URL`, `BROWSER`…).
- **Evidence:** failure screenshots attached to the scenario and saved to disk; Cucumber HTML/JSON/JUnit + **Extent Spark** reports; log4j2 log file.

## Run
```bash
./mvnw test                                        # all scenarios, headless Chrome
./mvnw test -Dcucumber.filter.tags="@smoke"        # smoke only
./mvnw test -Dbrowser=edge -Dheadless=false        # watch it in Edge
```
Reports: `test-output/ExtentReport.html`, `test-output/cucumber-report.html`.

## Layout
```
src/test/java/com/qa/
  pages/            BasePage, LoginPage, DashboardPage, AdminUsersPage
  stepdefinitions/  LoginSteps, NavigationSteps, AdminUserSteps
  hooks/            driver lifecycle + failure screenshots
  runners/          JUnit Cucumber runner
  utils/            ConfigReader, DriverManager, WaitUtil, ScreenshotUtil
src/test/resources/
  features/         login, navigation, admin_users
  config.properties, extent.properties, log4j2.xml
```

CI runs the suite headless on every push and on weekday mornings (GitHub Actions), uploading the reports as artifacts.
