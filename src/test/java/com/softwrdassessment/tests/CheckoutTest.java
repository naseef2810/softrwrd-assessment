package com.softwrdassessment.tests;

import com.softwrdassessment.base.BaseTest;
import com.softwrdassessment.pages.*;
import com.softwrdassessment.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CheckoutTest extends BaseTest {

    private CheckoutInfoPage checkoutInfoPage;

    @BeforeMethod(alwaysRun = true)
    public void setupCheckout() {
        Map<String, String> user = JsonReader.getUserByType("standard");
        new LoginPage(driver).login(user.get("username"), user.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addToCartByIndex(0);
        inventoryPage.addToCartByIndex(1);
        
        CartPage cartPage = inventoryPage.goToCart();
        checkoutInfoPage = cartPage.goToCheckout();
    }

    @Test(priority = 1, description = "Complete a full purchase with valid details and verify confirmation screen content")
    public void testFullPurchaseFlow() {
        checkoutInfoPage.enterDetails("John", "Doe", "12345");
        CheckoutOverviewPage overviewPage = checkoutInfoPage.continueToOverview();
        
        CheckoutCompletePage completePage = overviewPage.finishCheckout();
        Assert.assertTrue(completePage.getHeaderText().toLowerCase().contains("thank you"), "Confirmation header is missing or incorrect");
        Assert.assertTrue(completePage.getBodyText().toLowerCase().contains("dispatched"), "Confirmation text is missing or incorrect");
    }

    @DataProvider(name = "missingFieldsData")
    public Object[][] missingFieldsData() {
        return new Object[][]{
                {"", "Doe", "12345", "Error: First Name is required"},
                {"John", "", "12345", "Error: Last Name is required"},
                {"John", "Doe", "", "Error: Postal Code is required"}
        };
    }

    @Test(priority = 2, dataProvider = "missingFieldsData", description = "Checkout blocked when required fields are missing")
    public void testCheckoutBlockedMissingFields(String fName, String lName, String zip, String expectedError) {
        checkoutInfoPage.enterDetails(fName, lName, zip);
        checkoutInfoPage.clickContinue();
        Assert.assertEquals(checkoutInfoPage.getErrorMessage(), expectedError, "Validation error message did not match");
    }

    @Test(priority = 3, description = "Verify order summary is mathematically correct")
    public void testOrderSummaryMath() {
        checkoutInfoPage.enterDetails("Jane", "Smith", "98765");
        CheckoutOverviewPage overviewPage = checkoutInfoPage.continueToOverview();
        
        List<Double> itemPrices = overviewPage.getItemPrices();
        double expectedSubtotal = itemPrices.stream().mapToDouble(Double::doubleValue).sum();
        
        Assert.assertEquals(overviewPage.getSubtotal(), expectedSubtotal, "Subtotal does not match sum of item prices");
        
        double tax = overviewPage.getTax();
        double expectedTotal = expectedSubtotal + tax;
        
        Assert.assertEquals(overviewPage.getTotal(), expectedTotal, "Total does not match subtotal + tax");
    }
}
