package com.softwrdassessment.tests;

import com.softwrdassessment.base.BaseTest;
import com.softwrdassessment.pages.CartPage;
import com.softwrdassessment.pages.InventoryPage;
import com.softwrdassessment.pages.LoginPage;
import com.softwrdassessment.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpCartTest() {
        Map<String, String> user = JsonReader.getUserByType("standard");
        new LoginPage(driver).login(user.get("username"), user.get("password"));
        inventoryPage = new InventoryPage(driver);
    }

    @Test(priority = 1, description = "Add a single item and verify cart badge updates")
    public void testAddSingleItem() {
        inventoryPage.addToCartByIndex(0);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1", "Cart badge did not update correctly for 1 item");
    }

    @Test(priority = 2, description = "Add multiple items and verify all appear in cart")
    public void testAddMultipleItems() {
        inventoryPage.addToCartByIndex(0);
        inventoryPage.addToCartByIndex(1);
        inventoryPage.addToCartByIndex(2);
        
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "3", "Cart badge did not update for 3 items");
        
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Cart page does not show the correct number of added items");
    }

    @Test(priority = 3, description = "Remove an item from the cart and verify cart state")
    public void testRemoveItem() {
        inventoryPage.addToCartByIndex(0);
        inventoryPage.addToCartByIndex(1);
        
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Items were not added to cart successfully prior to removal");
        
        cartPage.removeItemByIndex(0);
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Item was not removed successfully from the cart");
    }

    @Test(priority = 4, description = "Cart persists across page navigation")
    public void testCartPersistence() {
        inventoryPage.addToCartByIndex(0);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1", "Setup failed");
        
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item is missing on Cart Page");
        
        cartPage.continueShopping();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1", "Cart badge lost count after navigating back from Cart Page");
        
        driver.navigate().refresh();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1", "Cart state did not persist after page refresh");
    }
}
