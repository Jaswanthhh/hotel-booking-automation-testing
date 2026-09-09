package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.ScreenshotUtils;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverFactory.getDriver();
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            String screenshotPath = ScreenshotUtils.takeScreenshot(driver, result.getName());
            System.out.println("Test failed. Screenshot captured at: " + screenshotPath);
        }
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
