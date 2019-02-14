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
    public void login() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");
        By userNameField = new By.ById("user-name");
        driver.findElement(userNameField).sendKeys("standard_user");
        By passwordField = new By.ById("password");
        driver.findElement(passwordField).sendKeys("secret_sauce");
        driver.findElement(By.className("login-button")).click();
        Thread.sleep(4000);
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}
