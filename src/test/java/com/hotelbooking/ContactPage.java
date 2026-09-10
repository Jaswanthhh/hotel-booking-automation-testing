package com.hotelbooking;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContactPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Contact form locators
    private final By nameField = By.id("name");
    private final By emailField = By.id("email");
    private final By phoneField = By.id("phone");
    private final By subjectField = By.id("subject");
    private final By messageField = By.id("description");

    private final By submitButton =
            By.xpath("//button[normalize-space()='Submit']");

    // Success message
    private final By successMessage =
            By.xpath("//h3[contains(normalize-space(), 'Thanks for getting in touch')]");

    // Validation/error message
    private final By validationMessage =
            By.cssSelector(".alert.alert-danger");


    // Constructor
    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    // Enter name
    public void enterName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField))
                .sendKeys(name);
    }


    // Enter email
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField))
                .sendKeys(email);
    }


    // Enter phone
    public void enterPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField))
                .sendKeys(phone);
    }


    // Enter subject
    public void enterSubject(String subject) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(subjectField))
                .sendKeys(subject);
    }


    // Enter message
    public void enterMessage(String message) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(messageField))
                .sendKeys(message);
    }


    // Fill the complete contact form
    public void fillContactForm(
            String name,
            String email,
            String phone,
            String subject,
            String message) {

        enterName(name);
        enterEmail(email);
        enterPhone(phone);
        enterSubject(subject);
        enterMessage(message);
    }


    // Click Submit button
    public void clickSubmit() {

        WebElement submit = wait.until(
                ExpectedConditions.presenceOfElementLocated(submitButton)
        );

        // Scroll the button into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                submit
        );

        wait.until(ExpectedConditions.visibilityOf(submit));

        // JavaScript click avoids the click interception issue
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                submit
        );
    }


    // Submit complete form
    public void submitForm(
            String name,
            String email,
            String phone,
            String subject,
            String message) {

        fillContactForm(name, email, phone, subject, message);
        clickSubmit();
    }


    // Check whether success message is displayed
    public boolean isSuccessMessageDisplayed() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(successMessage)
            ).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }


    // Get success message text
    public String getSuccessMessage() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage)
        ).getText();
    }


    // Check whether validation message is displayed
    public boolean isValidationMessageDisplayed() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(validationMessage)
            ).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }


    // Get validation/error message
    public String getValidationMessage() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(validationMessage)
        ).getText();
    }
}