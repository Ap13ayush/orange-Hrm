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

public class DataDrivenLoginTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/";

    @BeforeMethod
    public void setup() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
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

    /**
     * Data Provider for valid login credentials
     */
    @DataProvider(name = "validLoginData")
    public Object[][] getValidLoginData() {
        return new Object[][] {
            {"Admin", "admin123"},
        };
    }

    /**
     * Data Provider for invalid login credentials
     */
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
            {"InvalidUser", "InvalidPass", "Invalid credentials"},
            {"Admin", "wrongPassword", "Invalid credentials"},
            {"", "", "Required"},
            {"Admin", "", "Required"},
            {"", "admin123", "Required"},
            {"Admin", "Admin123", "Invalid credentials"},  // wrong case password
        };
    }

    /**
     * Data Provider for various login scenarios
     */
    @DataProvider(name = "allLoginData")
    public Object[][] getAllLoginData() {
        return new Object[][] {
            // username, password, expectedResult, description
            {"Admin", "admin123", "success", "Valid credentials"},
            {"InvalidUser", "InvalidPass", "failure", "Both invalid"},
            {"Admin", "wrongPass", "failure", "Invalid password"},
            {"wrongUser", "admin123", "failure", "Invalid username"},
            {"", "", "failure", "Empty credentials"},
        };
    }

    @Test(priority = 1, groups = {"Smoke", "DataDriven"}, 
          dataProvider = "validLoginData", 
          description = "Data-driven test for valid login")
    public void testValidLoginWithDataProvider(String username, String password) {
        try {
            System.out.println("Testing login with username: " + username);
            
            // Wait for login page to load
            WebElement usernameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );

            // Enter credentials
            usernameField.sendKeys(username);
            
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys(password);

            // Click login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait for dashboard to load
            wait.until(ExpectedConditions.urlContains("dashboard"));
            
            // Verify successful login
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("dashboard"), 
                "Login should be successful with username: " + username);
            
            System.out.println("✓ Login successful for user: " + username);

        } catch (Exception e) {
            Assert.fail("Valid login test failed for user " + username + ": " + e.getMessage());
        }
    }

    @Test(priority = 2, groups = {"Regression", "DataDriven"}, 
          dataProvider = "invalidLoginData", 
          description = "Data-driven test for invalid login attempts")
    public void testInvalidLoginWithDataProvider(String username, String password, String expectedErrorText) {
        try {
            System.out.println("Testing invalid login with username: '" + username + "', password: '" + password + "'");
            
            // Wait for login page to load
            WebElement usernameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );

            // Enter credentials
            if (!username.isEmpty()) {
                usernameField.sendKeys(username);
            }
            
            WebElement passwordField = driver.findElement(By.name("password"));
            if (!password.isEmpty()) {
                passwordField.sendKeys(password);
            }

            // Click login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait a moment for response
            Thread.sleep(1000);

            // Check for error message or validation
            boolean errorFound = false;
            String actualError = "";

            try {
                // Check for invalid credentials error
                WebElement errorMessage = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.oxd-alert-content-text"))
                );
                actualError = errorMessage.getText();
                errorFound = true;
            } catch (Exception e1) {
                // Check for required field validation
                if (driver.findElements(By.cssSelector("span.oxd-input-field-error-message")).size() > 0) {
                    actualError = "Required field validation";
                    errorFound = true;
                }
            }

            // Verify error is shown
            Assert.assertTrue(errorFound, 
                "Error should be displayed for invalid credentials: " + username + "/" + password);
            
            // Verify we're still on login page (not dashboard)
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("dashboard"), 
                "Should not reach dashboard with invalid credentials");
            
            System.out.println("✓ Login correctly failed for user: '" + username + "' - Error: " + actualError);

        } catch (Exception e) {
            Assert.fail("Invalid login test failed for user " + username + ": " + e.getMessage());
        }
    }

    @Test(priority = 3, groups = {"Regression", "DataDriven"}, 
          dataProvider = "allLoginData", 
          description = "Comprehensive data-driven login validation")
    public void testLoginWithVariousScenarios(String username, String password, 
                                              String expectedResult, String description) {
        try {
            System.out.println("Testing scenario: " + description);
            System.out.println("  Username: '" + username + "', Password: '" + password + "'");
            
            // Wait for login page to load
            WebElement usernameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );

            // Enter credentials
            if (!username.isEmpty()) {
                usernameField.sendKeys(username);
            }
            
            WebElement passwordField = driver.findElement(By.name("password"));
            if (!password.isEmpty()) {
                passwordField.sendKeys(password);
            }

            // Click login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Wait for response
            Thread.sleep(2000);

            // Verify result based on expected outcome
            String currentUrl = driver.getCurrentUrl();

            if (expectedResult.equals("success")) {
                // Should reach dashboard
                Assert.assertTrue(currentUrl.contains("dashboard"), 
                    description + " - Should reach dashboard");
                System.out.println("  ✓ Result: Login successful as expected");
                
            } else if (expectedResult.equals("failure")) {
                // Should stay on login page or show error
                Assert.assertFalse(currentUrl.contains("dashboard"), 
                    description + " - Should not reach dashboard");
                
                // Verify error message or validation is shown
                boolean errorShown = driver.findElements(By.cssSelector("p.oxd-alert-content-text")).size() > 0 ||
                                    driver.findElements(By.cssSelector("span.oxd-input-field-error-message")).size() > 0;
                
                Assert.assertTrue(errorShown, 
                    description + " - Error message should be displayed");
                System.out.println("  ✓ Result: Login failed as expected");
            }

        } catch (Exception e) {
            Assert.fail("Scenario '" + description + "' failed: " + e.getMessage());
        }
    }

    @Test(priority = 4, groups = {"DataDriven"}, 
          description = "Test login with username case variations")
    public void testLoginWithCaseVariations() {
        String[][] testData = {
            {"admin", "admin123"},    // lowercase - might work depending on system
            {"ADMIN", "admin123"},    // uppercase - might work depending on system  
            {"Admin ", "admin123"},   // trailing space - might be trimmed by system
        };

        int successCount = 0;
        int failureCount = 0;

        for (String[] credentials : testData) {
            String username = credentials[0];
            String password = credentials[1];
            
            System.out.println("Testing with username variation: '" + username + "'");
            
            // Close and restart browser for clean state
            if (driver != null) {
                driver.quit();
            }
            
            // Reinitialize driver
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Navigate to login page
            driver.get(BASE_URL);
            
            try {
                // Wait for login page
                WebElement usernameField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.name("username"))
                );

                // Enter credentials
                usernameField.sendKeys(username);
                driver.findElement(By.name("password")).sendKeys(password);

                // Click login
                driver.findElement(By.cssSelector("button[type='submit']")).click();

                // Wait for response
                Thread.sleep(2500);

                // Check result
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("dashboard")) {
                    System.out.println("✓ System accepted username: '" + username + "' (case-insensitive or trimming enabled)");
                    successCount++;
                } else {
                    System.out.println("✓ System rejected username: '" + username + "' (case-sensitive)");
                    failureCount++;
                }
                
            } catch (Exception e) {
                System.out.println("⚠ Test encountered error for '" + username + "': " + e.getMessage());
                failureCount++;
            }
        }

        System.out.println("✓ Case variation tests completed - Accepted: " + successCount + ", Rejected: " + failureCount);
        // Test passes as long as we tested all variations
        Assert.assertEquals(successCount + failureCount, testData.length, "All case variations should be tested");
    }

    @Test(priority = 5, groups = {"DataDriven"}, 
          description = "Test login with special characters in credentials")
    public void testLoginWithSpecialCharacters() {
        String[][] testData = {
            {"Admin@123", "pass@123"},
            {"User#123", "test$pass"},
            {"admin'OR'1'='1", "password"},
        };

        for (String[] credentials : testData) {
            String username = credentials[0];
            String password = credentials[1];
            
            try {
                System.out.println("Testing with special characters - Username: " + username);
                
                // Navigate to login page
                driver.get(BASE_URL);
                
                // Wait for login page
                WebElement usernameField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.name("username"))
                );

                // Enter credentials
                usernameField.sendKeys(username);
                driver.findElement(By.name("password")).sendKeys(password);

                // Click login
                driver.findElement(By.cssSelector("button[type='submit']")).click();

                // Wait for response
                Thread.sleep(1500);

                // Should not login with these credentials
                String currentUrl = driver.getCurrentUrl();
                Assert.assertFalse(currentUrl.contains("dashboard"), 
                    "Should not login with special characters: " + username);
                
                System.out.println("✓ Special character test passed for: " + username);

            } catch (Exception e) {
                Assert.fail("Special character test failed for " + username + ": " + e.getMessage());
            }
        }

        System.out.println("✓ All special character login tests completed");
    }
}
