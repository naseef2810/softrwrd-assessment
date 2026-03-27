package com.softwrdassessment.tests;

import com.softwrdassessment.base.BaseTest;
import com.softwrdassessment.pages.CheckoutInfoPage;
import com.softwrdassessment.pages.InventoryPage;
import com.softwrdassessment.pages.LoginPage;
import com.softwrdassessment.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class PerformanceTest extends BaseTest {

    @Test(priority = 1, description = "performance_glitch_user login completes successfully despite delay")
    public void testPerformanceGlitchUserLogin() {
        Map<String, String> user = JsonReader.getUserByType("performance");
        
        Instant start = Instant.now();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(user.get("username"), user.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load for performance_glitch_user");
        
        Instant end = Instant.now();
        long durationMs = Duration.between(start, end).toMillis();
        
        Assert.assertTrue(durationMs > 2000, "Expected a delay for performance_glitch_user but it loaded instantly");
    }

    @Test(priority = 2, description = "error_user encounters correct error states during checkout validation")
    public void testErrorUserActions() {
        Map<String, String> user = JsonReader.getUserByType("error");
        new LoginPage(driver).login(user.get("username"), user.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addToCartByIndex(0);
        
        CheckoutInfoPage checkoutInfoPage = inventoryPage.goToCart().goToCheckout();
        checkoutInfoPage.enterDetails("Jane", "Doe", "12345");
        checkoutInfoPage.clickContinue();
        
        try {
            checkoutInfoPage.getErrorMessage();
            Assert.fail("Expected error_user to fail showing validation error, but it showed one.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.assertTrue(true, "error_user successfully identified as silently failing validation.");
        }
    }
}
