package com.softwrdassessment.base;

import com.softwrdassessment.config.ConfigManager;
import com.softwrdassessment.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverFactory.initDriver();
        driver = WebDriverFactory.getDriver();
        driver.get(ConfigManager.getProperty("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }
}
