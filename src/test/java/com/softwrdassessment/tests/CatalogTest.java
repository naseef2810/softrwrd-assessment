package com.softwrdassessment.tests;

import com.softwrdassessment.base.BaseTest;
import com.softwrdassessment.pages.InventoryPage;
import com.softwrdassessment.pages.LoginPage;
import com.softwrdassessment.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogTest extends BaseTest {
    
    @BeforeMethod(alwaysRun = true)
    public void loginStandardUser(Method method) {
        if (!method.getName().equals("testProblemUserImages")) {
            Map<String, String> user = JsonReader.getUserByType("standard");
            new LoginPage(driver).login(user.get("username"), user.get("password"));
        }
    }

    @Test(priority = 1, description = "Product listing loads correctly")
    public void testProductListingLoads() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page is not loaded");
        
        int count = inventoryPage.getProductCount();
        Assert.assertTrue(count > 0, "No products are listed");
        
        List<String> names = inventoryPage.getProductNames();
        List<Double> prices = inventoryPage.getProductPrices();
        
        Assert.assertEquals(names.size(), count, "Not all products have names");
        Assert.assertEquals(prices.size(), count, "Not all products have prices");
        
        for (String name : names) {
            Assert.assertFalse(name.isEmpty(), "A product name is empty");
        }
    }

    @Test(priority = 2, description = "Sorting by Name A -> Z")
    public void testSortNameAZ() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.sortBy("az");
        
        List<String> names = inventoryPage.getProductNames();
        List<String> sortedNames = names.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(names, sortedNames, "Products are not sorted by Name A -> Z");
    }

    @Test(priority = 3, description = "Sorting by Name Z -> A")
    public void testSortNameZA() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.sortBy("za");
        
        List<String> names = inventoryPage.getProductNames();
        List<String> sortedNames = names.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Assert.assertEquals(names, sortedNames, "Products are not sorted by Name Z -> A");
    }

    @Test(priority = 4, description = "Sorting by Price Low -> High")
    public void testSortPriceLowToHigh() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.sortBy("lohi");
        
        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sortedPrices = prices.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(prices, sortedPrices, "Products are not sorted by Price Low -> High");
    }

    @Test(priority = 5, description = "Sorting by Price High -> Low")
    public void testSortPriceHighToLow() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.sortBy("hilo");
        
        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sortedPrices = prices.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Assert.assertEquals(prices, sortedPrices, "Products are not sorted by Price High -> Low");
    }

    @Test(priority = 6, description = "Problem user visual regression - mismatched images")
    public void testProblemUserImages() {
        Map<String, String> pUser = JsonReader.getUserByType("problem");
        new LoginPage(driver).login(pUser.get("username"), pUser.get("password"));
        
        InventoryPage inventoryPage = new InventoryPage(driver);
        List<String> imageSources = inventoryPage.getProductImageSources();
        
        boolean allSameImage = imageSources.stream().distinct().count() == 1;
        boolean hasBrokenUrl = imageSources.stream().anyMatch(src -> src.contains("sl-404") || src.contains("garbage"));
        Assert.assertTrue(allSameImage || hasBrokenUrl, "Detected problem_user visual regression: images are broken or identical");
    }
}
