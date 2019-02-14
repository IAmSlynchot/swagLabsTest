package storePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class testStorePage {
    public WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/ChromeDriver/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void openStorePage() {
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
    }

    @Test
    public void login() {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.className("login-button")).click();
        Assert.assertTrue(driver.findElement(By.className("product_label")).isDisplayed());
    }

    @Test
    public void buyAFleece() {
        // Navigate to main page and look at an item
        driver.get("https://www.saucedemo.com/inventory.html");
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
