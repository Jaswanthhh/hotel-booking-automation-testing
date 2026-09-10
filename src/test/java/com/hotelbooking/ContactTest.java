package com.hotelbooking;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ContactTest {

    private WebDriver driver;
    private ContactPage contactPage;

    private final String URL = "https://automationintesting.online/";


    // Setup browser before every test
    @BeforeMethod
    public void setUp() {

        driver = DriverFactory.createDriver();

        driver.get(URL);

        contactPage = new ContactPage(driver);
    }


    // Valid contact form
    @Test
    public void validContactForm() {

        contactPage.fillContactForm(
                "Aditya",
                "aditya@gmail.com",
                "4407123456789",
                "Test Subject",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed(),
                "Success message was not displayed"
        );
    }


    // Empty name
    @Test
    public void emptyName() {

        contactPage.fillContactForm(
                "",
                "aditya@gmail.com",
                "9876543210",
                "Test Subject",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for empty name"
        );
    }


    // Empty email
    @Test
    public void emptyEmail() {

        contactPage.fillContactForm(
                "Aditya",
                "",
                "9876543210",
                "Test Subject",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for empty email"
        );
    }


    // Invalid email
    @Test
    public void invalidEmail() {

        contactPage.fillContactForm(
                "Aditya",
                "aditya@",
                "9876543210",
                "Test Subject",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for invalid email"
        );
    }


    // Invalid phone
    @Test
    public void invalidPhone() {

        contactPage.fillContactForm(
                "Aditya",
                "aditya@gmail.com",
                "123",
                "Test Subject",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for invalid phone"
        );
    }


    // Empty subject
    @Test
    public void emptySubject() {

        contactPage.fillContactForm(
                "Aditya",
                "aditya@gmail.com",
                "9876543210",
                "",
                "This is a valid test message."
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for empty subject"
        );
    }


    // Empty message
    @Test
    public void emptyMessage() {

        contactPage.fillContactForm(
                "Aditya",
                "aditya@gmail.com",
                "9876543210",
                "Test Subject",
                ""
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for empty message"
        );
    }


    // Completely empty form
    @Test
    public void completelyEmptyForm() {

        contactPage.fillContactForm(
                "",
                "",
                "",
                "",
                ""
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isValidationMessageDisplayed(),
                "Validation message was not displayed for empty form"
        );
    }


    // Special characters
    @Test
    public void specialCharacters() {

        contactPage.fillContactForm(
                "@#$%",
                "test@gmail.com",
                "9876543210",
                "!@#$%",
                "@#$%^&*()"
        );

        contactPage.clickSubmit();

        // We only verify that the application responds
        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed()
                        || contactPage.isValidationMessageDisplayed(),
                "No success or validation message was displayed"
        );
    }


    // Long message
    @Test
    public void longMessage() {

        String longMessage =
                "This is a very long message used to test the contact form. "
                        + "This message is intentionally made longer than normal "
                        + "to verify how the application handles large amounts of text.";

        contactPage.fillContactForm(
                "Aditya",
                "aditya@gmail.com",
                "9876543210",
                "Long Message Test",
                longMessage
        );

        contactPage.clickSubmit();

        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed()
                        || contactPage.isValidationMessageDisplayed(),
                "No success or validation message was displayed"
        );
    }


    // Close browser after every test
    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}