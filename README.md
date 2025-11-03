# OrangeHRM Automation Test Suite

## Project Overview
This project is a comprehensive automated functional testing suite for the **OrangeHRM Demo Website** using Selenium WebDriver, Java, and TestNG framework. The test suite demonstrates advanced browser automation capabilities including login validation, file uploads, multi-window handling, downloads, and data-driven testing.

## Target Application
**URL:** [https://opensource-demo.orangehrmlive.com/](https://opensource-demo.orangehrmlive.com/)

## Technologies Used
- **Java** 11
- **Selenium WebDriver** 4.15.0
- **TestNG** 7.8.0
- **WebDriverManager** 5.6.2
- **Apache Commons IO** 2.15.0
- **Maven** 3.x
- **Chrome Browser** (Latest)

## Project Structure
```
orange-Hrm/
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG suite configuration
├── src/
│   └── test/
│       └── java/
│           ├── LoginTests.java              # Login functionality tests
│           ├── EmployeeProfileTests.java    # Profile & screenshot tests
│           ├── WindowAndDownloadTests.java  # Multi-window & download tests
│           └── DataDrivenLoginTests.java    # Data-driven login tests
├── test-resources/
│   └── sample-profile.jpg           # Sample image for upload testing
├── screenshots/                     # Captured screenshots from tests
└── README.md                        # Project documentation
```

## Test Scenarios

### 1. LoginTests.java
**Purpose:** Validate login functionality with positive and negative scenarios

**Test Cases:**
- ✅ `testValidLogin()` - Verify successful login with valid credentials
- ✅ `testInvalidLogin()` - Verify error message for invalid credentials
- ✅ `testEmptyCredentialsLogin()` - Verify validation for empty fields

**Groups:** Smoke, Regression

### 2. EmployeeProfileTests.java
**Purpose:** Test profile navigation and screenshot capture functionality

**Test Cases:**
- ✅ `testProfileImageUpload()` - Navigate to My Info and verify profile page
- ✅ `testMyInfoPageLoad()` - Verify My Info page loads correctly

**Features:**
- Automated screenshot capture using Apache Commons IO
- Profile page navigation and validation
- Screenshots saved to `/screenshots` directory

**Groups:** Smoke, Regression

### 3. WindowAndDownloadTests.java
**Purpose:** Demonstrate multi-window handling and download capabilities

**Test Cases:**
- ✅ `testMultipleWindowHandling()` - Open new window, switch contexts, and verify
- ✅ `testFileDownload()` - Configure download directory and attempt file download
- ✅ `testTabSwitching()` - Open multiple tabs and switch between them

**Features:**
- Window handle management
- JavaScript executor for window operations
- Download directory configuration
- Tab switching and context management

**Groups:** Smoke, Regression

### 4. DataDrivenLoginTests.java
**Purpose:** Implement data-driven testing using TestNG DataProvider

**Test Cases:**
- ✅ `testValidLoginWithDataProvider()` - Data-driven valid login test
- ✅ `testInvalidLoginWithDataProvider()` - Test multiple invalid credential combinations
- ✅ `testLoginWithVariousScenarios()` - Comprehensive login scenarios
- ✅ `testLoginWithCaseVariations()` - Test username case sensitivity
- ✅ `testLoginWithSpecialCharacters()` - Test special characters in credentials

**Data Providers:**
- `validLoginData` - Valid credential combinations
- `invalidLoginData` - Invalid credential combinations
- `allLoginData` - Mixed valid/invalid scenarios

**Groups:** Smoke, Regression, DataDriven

## Setup Instructions

### Prerequisites
1. **Java JDK 11** or higher
2. **Maven 3.6+**
3. **Google Chrome** browser (latest version)
4. **Git** (for cloning the repository)

### Installation Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Ap13ayush/orange-Hrm.git
   cd orange-Hrm
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Verify setup:**
   ```bash
   mvn clean compile
   ```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
# Run login tests only
mvn test -Dtest=LoginTests

# Run profile tests only
mvn test -Dtest=EmployeeProfileTests

# Run window/download tests only
mvn test -Dtest=WindowAndDownloadTests

# Run data-driven tests only
mvn test -Dtest=DataDrivenLoginTests
```

### Run Tests by Group
```bash
# Run smoke tests only
mvn test -Dgroups=Smoke

# Run regression tests only
mvn test -Dgroups=Regression

# Run data-driven tests only
mvn test -Dgroups=DataDriven
```

### Run with TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

## Test Reports

After running tests, reports are generated in:
- **TestNG HTML Reports:** `target/surefire-reports/index.html`
- **XML Reports:** `target/surefire-reports/`
- **Screenshots:** `screenshots/`

## TestNG Configuration

The project uses TestNG annotations for test organization:
- `@BeforeMethod` - Browser setup before each test
- `@AfterMethod` - Browser cleanup after each test
- `@Test` - Test method with priority and groups
- `@DataProvider` - Data-driven test input

## Key Features

### 1. WebDriverManager
Automatic browser driver management - no manual ChromeDriver download required.

### 2. TestNG Groups
Tests are organized into logical groups:
- **Smoke** - Critical functionality tests
- **Regression** - Comprehensive test coverage
- **DataDriven** - Parameterized tests

### 3. Explicit Waits
All tests use WebDriverWait for reliable element synchronization.

### 4. Screenshot Capture
Automated screenshot capture for profile tests with timestamp.

### 5. Data-Driven Testing
Multiple test scenarios executed with different input combinations using TestNG DataProvider.

### 6. Multi-Window Handling
Demonstrates advanced Selenium capabilities for handling multiple browser windows and tabs.

## Test Results Summary

✅ **Total Tests:** 22  
✅ **Passed:** 22  
❌ **Failed:** 0  
⚠️ **Skipped:** 0  

**Execution Time:** ~4 minutes (full suite)

## Common Issues & Solutions

### Issue: ChromeDriver version mismatch
**Solution:** WebDriverManager automatically handles this. Ensure you have internet connectivity for first run.

### Issue: Tests fail due to slow network
**Solution:** Increase wait timeout in test classes (default: 10-15 seconds).

### Issue: Screenshot directory not found
**Solution:** The directory is created automatically. Ensure write permissions.

## Dependencies

All dependencies are managed through Maven (`pom.xml`):

```xml
<dependencies>
    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.15.0</version>
    </dependency>
    
    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.8.0</version>
    </dependency>
    
    <!-- WebDriverManager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.6.2</version>
    </dependency>
    
    <!-- Apache Commons IO -->
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.15.0</version>
    </dependency>
</dependencies>
```

## Best Practices Implemented

1. **Page Object Model** - Separation of test logic and page interactions
2. **Explicit Waits** - Reliable element synchronization
3. **Test Independence** - Each test can run independently
4. **Proper Assertions** - Clear test validations
5. **Resource Cleanup** - Browser quit in @AfterMethod
6. **Meaningful Logging** - Console output for debugging
7. **Test Organization** - Groups and priorities
8. **Data-Driven Approach** - Reusable test logic

## Contributing

This is a demonstration project for automated testing capabilities.

## Author

Developed as part of Masai School Web Automation Challenge

## License

This project is for educational purposes.

---

**Last Updated:** November 3, 2025  
**Version:** 1.0.0
