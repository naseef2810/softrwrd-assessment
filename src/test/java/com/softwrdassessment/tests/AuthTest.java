package com.softwrdassessment.tests;

import com.softwrdassessment.base.BaseTest;
import com.softwrdassessment.pages.InventoryPage;
import com.softwrdassessment.pages.LoginPage;
import com.softwrdassessment.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class AuthTest extends BaseTest {

    @Test(priority = 1, description = "Successful login with valid credentials")
    public void testSuccessfulLogin() {
        Map<String, String> user = JsonReader.getUserByType("standard");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(user.get("username"), user.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load after valid login.");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][]{
                // username, password, expectedErrorMessagePart
                {JsonReader.getUserByType("invalid").get("username"), JsonReader.getUserByType("invalid").get("password"), "Username and password do not match"},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"},
                {JsonReader.getUserByType("sql_injection").get("username"), JsonReader.getUserByType("sql_injection").get("password"), "Username and password do not match"}
        };
    }

    @Test(priority = 2, dataProvider = "invalidLoginData", description = "Login failure with invalid credentials")
    public void testInvalidLogin(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed.");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError), "Error message did not contain: " + expectedError);
    }

    @Test(priority = 3, description = "Locked-out user behaviour")
    public void testLockedOutUser() {
        Map<String, String> user = JsonReader.getUserByType("locked_out");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(user.get("username"), user.get("password"));
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for locked out user.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"), "Error message did not indicate user is locked out.");
    }

    @Test(priority = 4, description = "Session persistence and logout behaviour")
    public void testSessionPersistenceAndLogout() {
        Map<String, String> user = JsonReader.getUserByType("standard");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(user.get("username"), user.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Not logged in successfully");

        // Refresh and check persistence
        driver.navigate().refresh();
        Assert.assertTrue(inventoryPage.isLoaded(), "Session did not persist after refresh");

        // Logout
        loginPage = inventoryPage.logout();
        
        // Ensure redirect to login
        Assert.assertTrue(loginPage.isLoaded(), "Did not redirect to login page after logout");
    }
}
