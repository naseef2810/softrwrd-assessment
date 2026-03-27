package com.softwrdassessment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInfoPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By zipInput = By.id("postal-code");
    private final By continueBtn = By.id("continue");
    private final By cancelBtn = By.id("cancel");
    private final By errorMsg = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public void enterDetails(String firstName, String lastName, String zipCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(zipInput, zipCode);
    }

    public CheckoutOverviewPage continueToOverview() {
        click(continueBtn);
        return new CheckoutOverviewPage(driver);
    }
    
    public void clickContinue() {
        click(continueBtn);
    }

    public String getErrorMessage() {
        return getText(errorMsg);
    }
}
