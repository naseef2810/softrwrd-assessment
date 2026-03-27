package com.softwrdassessment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By header = By.className("complete-header");
    private final By text = By.className("complete-text");
    private final By backHomeBtn = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getHeaderText() {
        return getText(header);
    }

    public String getBodyText() {
        return getText(text);
    }

    public InventoryPage backHome() {
        click(backHomeBtn);
        return new InventoryPage(driver);
    }
}
