package storePage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class testStorePage {
    private WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/ChromeDriver/chromedriver");
        driver = new ChromeDriver();
    }

    private void loginToStore() {
        driver.get("https://www.saucedemo.com/");
        // Auth details should never be tracked like this in a real test, but they're already written right out on the site
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.className("login-button")).click();
    }

    private void resetAppData() {
        try {
            // Open menu, click "reset app data" to clear cart, close menu
            driver.findElement(By.className("bm-burger-button")).click();
            Thread.sleep(300); // pause for sidebar to open
            driver.findElement(By.id("reset_sidebar_link")).click();
            driver.findElement(By.className("bm-cross-button")).click();
            Thread.sleep(300); // pause for sidebar to close
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Test
    public void testOpenStorePage() {
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
    }

    @Test
    public void testLogin() {
        loginToStore();
        Assert.assertTrue(driver.findElement(By.className("product_label")).isDisplayed());
    }

    @Test
    public void testBadLogin() {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("fake_user");
        driver.findElement(By.id("password")).sendKeys("something_fake");
        driver.findElement(By.className("login-button")).click();
        String errorText = driver.findElement(By.cssSelector("[data-test=\"error\"]")).getText();
        Assert.assertEquals(errorText, "Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void testAddAndRemoveItem() {
        loginToStore();
        resetAppData();
        driver.findElement(By.id("item_0_title_link")).click();
        String itemName = driver.findElement(By.className("inventory_details_name")).getText();
        Assert.assertEquals(itemName, "Sauce Labs Bike Light");

        // Add item to cart and go to cart page
        driver.findElement(By.className("add-to-cart-button")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertTrue(driver.findElement(By.className("cart_item")).isDisplayed());
        String cartItemName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(cartItemName, "Sauce Labs Bike Light");

        // Remove item from cart and ensure that it is gone
        driver.findElement(By.className("remove-from-cart-button")).click();
        Assert.assertFalse(isElementPresent(By.className("inventory_item_name")));
    }

    @Test
    public void testBuyAFleece() {
        // Navigate to main page and look at an item
        loginToStore();
        resetAppData();
        driver.findElement(By.id("item_5_title_link")).click();
        String itemName = driver.findElement(By.className("inventory_details_name")).getText();
        Assert.assertEquals(itemName, "Sauce Labs Fleece Jacket");

        // Add item to cart and go to cart page
        driver.findElement(By.className("add-to-cart-button")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertTrue(driver.findElement(By.className("cart_item")).isDisplayed());
        String cartItemName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(cartItemName, "Sauce Labs Fleece Jacket");

        // Begin checkout process and add personal info
        driver.findElement(By.className("cart_checkout_link")).click();
        driver.findElement(By.cssSelector("[data-test=\"firstName\"]")).sendKeys("Bruce");
        driver.findElement(By.cssSelector("[data-test=\"lastName\"]")).sendKeys("Wayne");
        driver.findElement(By.cssSelector("[data-test=\"postalCode\"]")).sendKeys("53540");
        driver.findElement(By.className("cart_checkout_link")).click();
        String overviewHeaderText = driver.findElement(By.className("subheader_label")).getText();
        Assert.assertEquals(overviewHeaderText, "Checkout: Overview");

        // Complete checkout process and verify complete
        Assert.assertTrue(driver.findElement(By.className("cart_item")).isDisplayed());
        String overviewItemName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(overviewItemName, "Sauce Labs Fleece Jacket");
        driver.findElement(By.className("cart_checkout_link")).click();
        Assert.assertTrue(driver.findElement(By.className("complete-header")).isDisplayed());
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}
