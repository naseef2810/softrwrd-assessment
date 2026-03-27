package com.softwrdassessment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By addToCartButtons = By.cssSelector(".btn_inventory");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By itemImages = By.cssSelector(".inventory_item_img img");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return driver.findElements(title).size() > 0 && waitForVisible(title).isDisplayed();
    }

    public int getProductCount() {
        return waitForAllVisible(inventoryItems).size();
    }

    public List<String> getProductNames() {
        return waitForAllVisible(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {
        return waitForAllVisible(itemPrices).stream()
                .map(WebElement::getText)
                .map(t -> Double.parseDouble(t.replace("$", "")))
                .collect(Collectors.toList());
    }

    public void addToCartByIndex(int index) {
        waitForAllVisible(addToCartButtons).get(index).click();
    }

    public void clickInventoryButtonByIndex(int index) {
        waitForAllVisible(addToCartButtons).get(index).click();
    }

    public String getCartBadgeCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return "0";
        }
        return getText(cartBadge);
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public void sortBy(String value) {
        Select select = new Select(waitForVisible(sortDropdown));
        select.selectByValue(value); // az, za, lohi, hilo
    }

    public List<String> getProductImageSources() {
        return waitForAllVisible(itemImages).stream()
                .map(img -> img.getAttribute("src"))
                .collect(Collectors.toList());
    }

    public LoginPage logout() {
        click(menuButton);
        WebElement element = waitForVisible(logoutLink);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        return new LoginPage(driver);
    }
}
