package com.qa.pages;

import com.qa.utils.WaitUtil;
import org.openqa.selenium.By;

import java.util.List;

/** The logged-in shell: header, side menu and user menu are shared by every module page. */
public class DashboardPage extends BasePage {
    private static final By HEADER = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private static final By MENU_ITEMS = By.cssSelector(".oxd-main-menu-item span");
    private static final By USER_MENU = By.cssSelector(".oxd-userdropdown-tab");
    private static final By LOGOUT = By.xpath("//a[normalize-space()='Logout']");

    public boolean isLoaded() {
        WaitUtil.waitForUrl("/dashboard/index");
        return getHeader().equals("Dashboard");
    }

    public String getHeader() {
        return getText(HEADER);
    }

    public List<String> getMenuItems() {
        return getTexts(MENU_ITEMS);
    }

    public void openModule(String name) {
        click(By.xpath("//a[contains(@class,'oxd-main-menu-item')][normalize-space()='" + name + "']"));
    }

    public void logout() {
        click(USER_MENU);
        click(LOGOUT);
    }
}
