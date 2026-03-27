package com.softwrdassessment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage extends BasePage {

    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishBtn = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public List<Double> getItemPrices() {
        return waitForAllVisible(itemPrices).stream()
                .map(WebElement::getText)
                .map(text -> Double.parseDouble(text.replace("$", "")))
                .collect(Collectors.toList());
    }

    public double getSubtotal() {
        String text = getText(subtotalLabel);
        return parseAmount(text);
    }

    public double getTax() {
        String text = getText(taxLabel);
        return parseAmount(text);
    }

    public double getTotal() {
        String text = getText(totalLabel);
        return parseAmount(text);
    }

    private double parseAmount(String text) {
        return Double.parseDouble(text.replaceFirst(".*\\$", ""));
    }

    public CheckoutCompletePage finishCheckout() {
        click(finishBtn);
        return new CheckoutCompletePage(driver);
    }
}
