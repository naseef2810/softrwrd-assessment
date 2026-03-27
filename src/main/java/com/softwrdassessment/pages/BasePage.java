package com.softwrdassessment.pages;

import com.softwrdassessment.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForElementVisible(driver, locator);
    }

    protected WebElement waitForVisible(WebElement element) {
        return WaitUtils.waitForElementVisible(driver, element);
    }

    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForElementClickable(driver, locator);
    }

    protected WebElement waitForClickable(WebElement element) {
        return WaitUtils.waitForElementClickable(driver, element);
    }
    
    protected List<WebElement> waitForAllVisible(By locator) {
        return WaitUtils.waitForElementsVisible(driver, locator);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void click(WebElement element) {
        waitForClickable(element).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected String getText(WebElement element) {
        return waitForVisible(element).getText();
    }
}
