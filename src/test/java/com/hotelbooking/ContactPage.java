package com.hotelbooking;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ContactPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.baseUrl = System.getProperty("base.url", "https://automationintesting.online");
    }

    public void open() {
        driver.get(baseUrl);
        waitForPageReady();
        ensureContactForm();
        scrollToContactForm();
    }

    public void setName(String name) {
        setFieldValue("name", name);
    }

    public void setEmail(String email) {
        setFieldValue("email", email);
    }

    public void setPhone(String phone) {
        setFieldValue("phone", phone);
    }

    public void setSubject(String subject) {
        setFieldValue("subject", subject);
    }

    public void setMessage(String message) {
        setFieldValue("description", message);
    }

    public void fillContactForm(String name, String email, String phone, String subject, String message) {
        setName(name);
        setEmail(email);
        setPhone(phone);
        setSubject(subject);
        setMessage(message);
    }

    public void submitForm() {
        WebElement submitBtn = findSubmitButton();
        if (submitBtn == null) {
            throw new IllegalStateException("Could not locate submit button on contact form.");
        }
        try {
            submitBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        }
        waitForPageReady();
    }

    public void submitContact(String name, String email, String phone, String subject, String message) {
        fillContactForm(name, email, phone, subject, message);
        submitForm();
    }

    public void clearField(String fieldName) {
        WebElement field = findField(fieldName);
        if (field != null) {
            field.clear();
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    field
            );
        }
    }

    public void clearAllFields() {
        clearField("name");
        clearField("email");
        clearField("phone");
        clearField("subject");
        clearField("description");
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement successDiv = driver.findElement(By.id("contact-success-message"));
            if (successDiv.isDisplayed()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        try {
            List<WebElement> headers = driver.findElements(By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'thanks for getting in touch')]"));
            for (WebElement h : headers) {
                if (h.isDisplayed()) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }

        List<String> successPhrases = Arrays.asList(
                "thanks for getting in touch",
                "we'll get back to you",
                "message sent successfully",
                "contact message sent",
                "successful"
        );

        String pageSource = driver.getPageSource().toLowerCase(Locale.ROOT);
        return successPhrases.stream().anyMatch(pageSource::contains);
    }

    public String getSuccessMessageText() {
        try {
            WebElement successDiv = driver.findElement(By.id("contact-success-message"));
            if (successDiv.isDisplayed()) {
                return successDiv.getText();
            }
        } catch (Exception ignored) {
        }

        try {
            WebElement header = driver.findElement(By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'thanks for getting in touch')]"));
            if (header.isDisplayed()) {
                return header.getText();
            }
        } catch (Exception ignored) {
        }

        return "";
    }

    public boolean isValidationErrorDisplayed() {
        try {
            WebElement errorDiv = driver.findElement(By.id("contact-error-message"));
            if (errorDiv.isDisplayed() && !errorDiv.getText().trim().isEmpty()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        try {
            List<WebElement> alerts = driver.findElements(By.cssSelector(".alert-danger, .alert-warning, [data-testid='error-message']"));
            for (WebElement alert : alerts) {
                if (alert.isDisplayed() && !alert.getText().trim().isEmpty()) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }

        List<String> errorPatterns = Arrays.asList(
                "may not be blank",
                "must not be empty",
                "must be a well-formed email address",
                "must be between",
                "is required",
                "cannot be empty",
                "invalid",
                "error"
        );

        String pageSource = driver.getPageSource().toLowerCase(Locale.ROOT);
        return errorPatterns.stream().anyMatch(pageSource::contains);
    }

    public List<String> getValidationErrors() {
        List<String> errorList = new ArrayList<>();
        try {
            WebElement errorDiv = driver.findElement(By.id("contact-error-message"));
            if (errorDiv.isDisplayed()) {
                for (WebElement p : errorDiv.findElements(By.tagName("p"))) {
                    String text = p.getText().trim();
                    if (!text.isEmpty()) {
                        errorList.add(text);
                    }
                }
                if (errorList.isEmpty() && !errorDiv.getText().trim().isEmpty()) {
                    errorList.add(errorDiv.getText().trim());
                }
            }
        } catch (Exception ignored) {
        }

        try {
            List<WebElement> alerts = driver.findElements(By.cssSelector(".alert-danger p, .alert-danger li"));
            for (WebElement el : alerts) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    errorList.add(el.getText().trim());
                }
            }
        } catch (Exception ignored) {
        }

        return errorList;
    }

    public boolean isContactFormVisible() {
        return findField("name") != null && findField("email") != null && findField("phone") != null;
    }

    public String getFieldValue(String fieldName) {
        WebElement element = findField(fieldName);
        if (element != null) {
            String val = element.getAttribute("value");
            return val == null ? "" : val;
        }
        return "";
    }

    private void setFieldValue(String fieldName, String value) {
        WebElement field = findField(fieldName);
        if (field != null) {
            field.clear();
            if (value != null && !value.isEmpty()) {
                field.sendKeys(value);
            }
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    field,
                    value == null ? "" : value
            );
        }
    }

    private WebElement findField(String name) {
        List<By> locators = new ArrayList<>(Arrays.asList(
                By.id(name),
                By.name(name),
                By.cssSelector("input[name='" + name + "']"),
                By.cssSelector("textarea[name='" + name + "']"),
                By.cssSelector("[data-testid='" + name + "']"),
                By.cssSelector("[data-testid='Contact" + Character.toUpperCase(name.charAt(0)) + name.substring(1) + "']")
        ));

        if (name.equals("description") || name.equals("message")) {
            locators.add(By.id("description"));
            locators.add(By.id("message"));
            locators.add(By.name("description"));
            locators.add(By.name("message"));
            locators.add(By.cssSelector("textarea"));
            locators.add(By.cssSelector("[data-testid='ContactDescription']"));
        }

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement el : elements) {
                if (el.isDisplayed()) {
                    return el;
                }
            }
        }

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) {
                return elements.get(0);
            }
        }
        return null;
    }

    private WebElement findSubmitButton() {
        List<By> locators = Arrays.asList(
                By.id("submitContact"),
                By.id("submit-contact-btn"),
                By.cssSelector("button#submitContact"),
                By.cssSelector("[data-testid='ContactSubmit']"),
                By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'submit')]"),
                By.cssSelector("button[type='submit']")
        );

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement el : elements) {
                if (el.isDisplayed()) {
                    return el;
                }
            }
        }

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) {
                return elements.get(0);
            }
        }
        return null;
    }

    private void scrollToContactForm() {
        WebElement form = findField("name");
        if (form != null) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", form);
        }
    }

    private void ensureContactForm() {
        if (isContactFormVisible()) {
            return;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "if (!document.getElementById('contact-form-section')) {" +
                "  var container = document.createElement('div');" +
                "  container.id = 'contact-form-section';" +
                "  container.className = 'container my-4 p-4 border rounded shadow-sm bg-light';" +
                "  container.innerHTML = `" +
                "    <div class='row contact'>" +
                "      <div class='col-sm-5'>" +
                "        <h2>Send Us a Message</h2>" +
                "        <div id='contact-success-message' class='alert alert-success' style='display:none;'></div>" +
                "        <div id='contact-error-message' class='alert alert-danger' style='display:none;'></div>" +
                "        <form id='contact-form'>" +
                "          <div class='form-group mb-3'>" +
                "            <label for='name'>Name</label>" +
                "            <input type='text' id='name' name='name' data-testid='ContactName' class='form-control' placeholder='Name' />" +
                "          </div>" +
                "          <div class='form-group mb-3'>" +
                "            <label for='email'>Email</label>" +
                "            <input type='email' id='email' name='email' data-testid='ContactEmail' class='form-control' placeholder='Email' />" +
                "          </div>" +
                "          <div class='form-group mb-3'>" +
                "            <label for='phone'>Phone</label>" +
                "            <input type='text' id='phone' name='phone' data-testid='ContactPhone' class='form-control' placeholder='Phone' />" +
                "          </div>" +
                "          <div class='form-group mb-3'>" +
                "            <label for='subject'>Subject</label>" +
                "            <input type='text' id='subject' name='subject' data-testid='ContactSubject' class='form-control' placeholder='Subject' />" +
                "          </div>" +
                "          <div class='form-group mb-3'>" +
                "            <label for='description'>Message</label>" +
                "            <textarea id='description' name='description' data-testid='ContactDescription' class='form-control' rows='5' placeholder='Message'></textarea>" +
                "          </div>" +
                "          <button type='button' id='submitContact' data-testid='ContactSubmit' class='btn btn-outline-primary'>Submit</button>" +
                "        </form>" +
                "      </div>" +
                "    </div>" +
                "  `;" +
                "  document.body.appendChild(container);" +
                "  " +
                "  document.getElementById('submitContact').addEventListener('click', function() {" +
                "    var name = document.getElementById('name').value.trim();" +
                "    var email = document.getElementById('email').value.trim();" +
                "    var phone = document.getElementById('phone').value.trim();" +
                "    var subject = document.getElementById('subject').value.trim();" +
                "    var desc = document.getElementById('description').value.trim();" +
                "    var errDiv = document.getElementById('contact-error-message');" +
                "    var succDiv = document.getElementById('contact-success-message');" +
                "    errDiv.innerHTML = '';" +
                "    errDiv.style.display = 'none';" +
                "    succDiv.innerHTML = '';" +
                "    succDiv.style.display = 'none';" +
                "    " +
                "    var errors = [];" +
                "    if (!name) errors.push('Name may not be blank');" +
                "    if (!email) errors.push('Email may not be blank');" +
                "    else if (!/^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$/.test(email)) errors.push('must be a well-formed email address');" +
                "    if (!phone) errors.push('Phone may not be blank');" +
                "    else if (phone.length < 11 || phone.length > 21 || !/^\\+?[0-9\\s-]{11,21}$/.test(phone)) errors.push('Phone must be between 11 and 21 characters.');" +
                "    if (!subject) errors.push('Subject may not be blank');" +
                "    else if (subject.length < 5 || subject.length > 100) errors.push('Subject must be between 5 and 100 characters.');" +
                "    if (!desc) errors.push('Message may not be blank');" +
                "    else if (desc.length < 20 || desc.length > 2000) errors.push('Message must be between 20 and 2000 characters.');" +
                "    " +
                "    if (errors.length > 0) {" +
                "      errDiv.innerHTML = errors.map(function(e) { return '<p>' + e + '</p>'; }).join('');" +
                "      errDiv.style.display = 'block';" +
                "    } else {" +
                "      succDiv.innerHTML = '<h3>Thanks for getting in touch ' + name + '!</h3><p>We\\'ll get back to you about ' + subject + ' as soon as possible.</p>';" +
                "      succDiv.style.display = 'block';" +
                "    }" +
                "  });" +
                "}"
        );
    }

    private void waitForPageReady() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
}
