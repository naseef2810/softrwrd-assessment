package com.softwrdassessment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cart_item");
    private final By removeButtons = By.cssSelector(".cart_button");
    private final By continueShoppingBtn = By.id("continue-shopping");
    private final By checkoutBtn = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void removeItemByIndex(int index) {
        waitForAllVisible(removeButtons).get(index).click();
    }

    public InventoryPage continueShopping() {
        click(continueShoppingBtn);
        return new InventoryPage(driver);
    }

    public CheckoutInfoPage goToCheckout() {
        click(checkoutBtn);
        return new CheckoutInfoPage(driver);
    }
}
