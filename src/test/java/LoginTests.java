import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/";
    private static final String VALID_USERNAME = "Admin";
    private static final String VALID_PASSWORD = "admin123";

    @BeforeMethod
    public void setup() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options for headless mode (optional)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Navigate to OrangeHRM
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void teardown() {
        // Close browser
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1, groups = {"Smoke", "Regression"}, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        try {
            // Wait for login page to load
            WebElement usernameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );

            // Enter valid username
            usernameField.sendKeys(VALID_USERNAME);

            // Enter valid password
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys(VALID_PASSWORD);

            // Click login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait for dashboard to load and verify
            WebElement dashboard = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("h6.oxd-text--h6"))
            );

            // Verify dashboard is displayed
            Assert.assertTrue(dashboard.isDisplayed(), "Dashboard should be displayed after successful login");
            
            // Verify page title or URL contains dashboard
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("dashboard"), 
                "URL should contain 'dashboard' after successful login. Current URL: " + currentUrl);

            System.out.println("✓ Valid login test passed - Dashboard loaded successfully");

        } catch (Exception e) {
            Assert.fail("Valid login test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, groups = {"Regression"}, description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        try {
            // Wait for login page to load
            WebElement usernameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );

            // Enter invalid username
            usernameField.sendKeys("InvalidUser");

            // Enter invalid password
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("InvalidPassword123");

            // Click login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait for error message to appear
            WebElement errorMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.oxd-alert-content-text"))
            );

            // Verify error message is displayed
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed for invalid login");

            // Verify error message text
            String errorText = errorMessage.getText();
            Assert.assertTrue(errorText.contains("Invalid credentials") || errorText.contains("invalid"), 
                "Error message should indicate invalid credentials. Actual message: " + errorText);

            System.out.println("✓ Invalid login test passed - Error message displayed: " + errorText);

        } catch (Exception e) {
            Assert.fail("Invalid login test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3, groups = {"Regression"}, description = "Verify login fails with empty credentials")
    public void testEmptyCredentialsLogin() {
        try {
            // Wait for login page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

            // Click login button without entering credentials
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait for required field messages
            Thread.sleep(1000); // Small wait for validation messages

            // Verify validation messages appear
            boolean validationPresent = driver.findElements(By.cssSelector("span.oxd-input-field-error-message")).size() > 0;
            Assert.assertTrue(validationPresent, "Required field validation messages should appear");

            System.out.println("✓ Empty credentials test passed - Validation messages displayed");

        } catch (Exception e) {
            Assert.fail("Empty credentials test failed: " + e.getMessage());
        }
    }
}
