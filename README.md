# hotel-booking-automation-testing
Selenium + TestNG automation framework for Hotel Booking - Automation in Testing Online (https://automationintesting.online/).

## Customer Profile Automation
This test suite includes automated tests for the customer profile workflow using Selenium WebDriver and TestNG:
1. **Update Customer Profile**: Successfully update customer profile fields (Address, City, Phone) with valid new data.
2. **Persist Profile Updates**: Verify updated profile information persists after navigating away and returning to the profile page.
3. **Mandatory Field Validation**: Verify form shows error when mandatory fields are cleared and submitted.

## Contact Form Automation
This test suite includes comprehensive tests for the contact form (`ContactPage.java`, `ContactTest.java`):
1. **Valid Contact Form**: Automate valid submission and verify confirmation message.
2. **Empty Name Validation**: Assert error when name is blank.
3. **Empty Email Validation**: Assert error when email is blank.
4. **Invalid Email Format**: Assert error when email syntax is invalid.
5. **Invalid Phone Validation**: Assert error when phone number length/format is invalid.
6. **Empty Subject Validation**: Assert error when subject is blank.
7. **Empty Message Validation**: Assert error when message description is blank.
8. **Completely Empty Form**: Assert multiple validation errors on submitting an empty form.
9. **Special Characters**: Verify submission with valid special characters in fields.
10. **Long Message**: Verify submission with long message body.

## Prerequisites
- Java 17+
- Maven 3.8+
- Google Chrome or Microsoft Edge

## How to Run Tests

Run all tests via TestNG suite:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=CustomerProfileUpdateTest
```

Run on Edge browser:
```bash
mvn test -Dbrowser=edge
```
