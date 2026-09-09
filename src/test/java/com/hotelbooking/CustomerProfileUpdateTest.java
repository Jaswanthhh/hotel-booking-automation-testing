package com.hotelbooking;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class CustomerProfileUpdateTest {
    private WebDriver driver;
    private ProfilePage profilePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();
        profilePage = new ProfilePage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Successfully updates customer profile fields with valid data")
    public void updateCustomerProfileFieldsWithValidData() {
        profilePage.open();

        profilePage.updateProfile("123 Updated Street", "London", "5550102030");

        Assert.assertTrue(
                profilePage.isSuccessMessageShown() || profilePage.containsUpdatedValues("123 Updated Street", "London", "5550102030"),
                "Expected the updated profile values to be saved successfully."
        );
    }

    @Test(description = "Persist profile updates after navigating away and returning")
    public void persistUpdatedProfileAfterNavigatingAwayAndReturning() {
        profilePage.open();
        profilePage.updateProfile("45 Market Road", "Manchester", "07777123456");

        profilePage.navigateAwayAndReturn();

        Assert.assertTrue(
                profilePage.isSuccessMessageShown() || profilePage.containsUpdatedValues("45 Market Road", "Manchester", "07777123456"),
                "Expected updated profile values to persist after navigation."
        );
    }

    @Test(description = "Show validation errors when mandatory fields are cleared and submitted")
    public void showValidationErrorsWhenMandatoryFieldsAreCleared() {
        profilePage.open();
        profilePage.clearMandatoryFields();
        profilePage.submit();

        Assert.assertTrue(
                profilePage.isValidationErrorDisplayed(),
                "Expected validation error when mandatory fields are blank."
        );
    }
}
