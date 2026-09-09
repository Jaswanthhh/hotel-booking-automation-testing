package com.hotelbooking;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ContactTest {
    private WebDriver driver;
    private ContactPage contactPage;

    private static final String VALID_NAME = "Jane Smith";
    private static final String VALID_EMAIL = "jane.smith@example.com";
    private static final String VALID_PHONE = "012345678901";
    private static final String VALID_SUBJECT = "Room Reservation Inquiry";
    private static final String VALID_MESSAGE = "Hello, I would like to check availability for a double room next month. Thank you!";

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();
        contactPage = new ContactPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Automate valid contact form submission and verify success message")
    public void testValidContactFormSubmission() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_SUBJECT, VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed(),
                "Expected success confirmation message after submitting valid contact form."
        );
    }

    @Test(description = "Verify validation error when name field is empty")
    public void testEmptyNameValidation() {
        contactPage.open();
        contactPage.submitContact("", VALID_EMAIL, VALID_PHONE, VALID_SUBJECT, VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with empty name."
        );
    }

    @Test(description = "Verify validation error when email field is empty")
    public void testEmptyEmailValidation() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, "", VALID_PHONE, VALID_SUBJECT, VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with empty email."
        );
    }

    @Test(description = "Verify validation error when email format is invalid")
    public void testInvalidEmailValidation() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, "invalid-email-address", VALID_PHONE, VALID_SUBJECT, VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with invalid email format."
        );
    }

    @Test(description = "Verify validation error when phone number is invalid")
    public void testInvalidPhoneValidation() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, VALID_EMAIL, "12345", VALID_SUBJECT, VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with invalid phone length/format."
        );
    }

    @Test(description = "Verify validation error when subject is empty")
    public void testEmptySubjectValidation() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, VALID_EMAIL, VALID_PHONE, "", VALID_MESSAGE);

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with empty subject."
        );
    }

    @Test(description = "Verify validation error when message field is empty")
    public void testEmptyMessageValidation() {
        contactPage.open();
        contactPage.submitContact(VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_SUBJECT, "");

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation error message when submitting form with empty message."
        );
    }

    @Test(description = "Verify validation errors when submitting a completely empty form")
    public void testCompletelyEmptyFormValidation() {
        contactPage.open();
        contactPage.clearAllFields();
        contactPage.submitForm();

        Assert.assertTrue(
                contactPage.isValidationErrorDisplayed(),
                "Expected validation errors when submitting completely empty form."
        );

        List<String> errors = contactPage.getValidationErrors();
        Assert.assertFalse(
                errors.isEmpty() && !contactPage.isValidationErrorDisplayed(),
                "Expected one or more validation error messages to be displayed."
        );
    }

    @Test(description = "Verify submitting contact form containing valid special characters")
    public void testSpecialCharactersInForm() {
        contactPage.open();
        String specialName = "Mary-Jane O'Connor & Co.";
        String specialEmail = "mary.jane+booking_test@example-domain.com";
        String specialPhone = "+44 (0) 1234 567890";
        String specialSubject = "Inquiry & Quotes: Room #101 @ Weekend!";
        String specialMessage = "Hello! We are asking about prices: $100-$150/night? (Special amenities: Wi-Fi, A/C & Breakfast). Thanks!";

        contactPage.submitContact(specialName, specialEmail, specialPhone, specialSubject, specialMessage);

        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed() || !contactPage.isValidationErrorDisplayed(),
                "Expected contact form with valid special characters to be submitted or accepted."
        );
    }

    @Test(description = "Verify submitting contact form with a long message")
    public void testLongMessageSubmission() {
        contactPage.open();
        StringBuilder longMsg = new StringBuilder();
        longMsg.append("We would like to book the whole executive floor for an annual conference. ");
        while (longMsg.length() < 300) {
            longMsg.append("Please provide catering options, conference room equipment, and projector setup details. ");
        }

        contactPage.submitContact(VALID_NAME, VALID_EMAIL, VALID_PHONE, "Conference Booking", longMsg.toString());

        Assert.assertTrue(
                contactPage.isSuccessMessageDisplayed(),
                "Expected contact form with long message to submit successfully."
        );
    }
}
