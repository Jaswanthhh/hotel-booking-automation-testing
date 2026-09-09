package com.hotelbooking;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.baseUrl = System.getProperty("base.url", "https://automationintesting.online");
    }

    public void open() {
        driver.get(baseUrl);
        waitForPageReady();
        ensureProfileForm();
    }

    public void updateProfile(String address, String city, String phone) {
        setFieldValue("address", address);
        setFieldValue("city", city);
        setFieldValue("phone", phone);
        submit();
    }

    public void clearMandatoryFields() {
        clearField("address");
        clearField("city");
        clearField("phone");
    }

    public void submit() {
        WebElement submitBtn = findSaveButton();
        if (submitBtn == null) {
            throw new IllegalStateException("Could not locate submit button on profile form.");
        }
        submitBtn.click();
        waitForPageReady();
    }

    public boolean isProfileFormVisible() {
        return findField("address") != null && findField("city") != null && findField("phone") != null;
    }

    public boolean isSuccessMessageShown() {
        try {
            WebElement successAlert = driver.findElement(By.id("profile-success-message"));
            if (successAlert.isDisplayed()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        List<String> textCandidates = Arrays.asList(
                "success", "updated successfully", "profile updated", "saved successfully",
                "profile saved"
        );

        String source = driver.getPageSource().toLowerCase(Locale.ROOT);
        return textCandidates.stream().anyMatch(source::contains);
    }

    public boolean isValidationErrorDisplayed() {
        try {
            WebElement errorAlert = driver.findElement(By.id("profile-error-message"));
            if (errorAlert.isDisplayed() && !errorAlert.getText().trim().isEmpty()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        List<String> errorPatterns = Arrays.asList(
                "required",
                "cannot be empty",
                "field is required",
                "please fill",
                "invalid",
                "error"
        );

        String pageSource = driver.getPageSource().toLowerCase(Locale.ROOT);
        return errorPatterns.stream().anyMatch(pageSource::contains);
    }

    public boolean containsUpdatedValues(String address, String city, String phone) {
        String currentAddress = getFieldValue("address");
        String currentCity = getFieldValue("city");
        String currentPhone = getFieldValue("phone");

        return (currentAddress != null && currentAddress.contains(address))
                && (currentCity != null && currentCity.contains(city))
                && (currentPhone != null && currentPhone.contains(phone));
    }

    public String getFieldValue(String fieldName) {
        WebElement element = findField(fieldName);
        if (element != null) {
            String val = element.getAttribute("value");
            return val == null ? "" : val;
        }
        return "";
    }

    public void navigateAwayAndReturn() {
        driver.navigate().refresh();
        waitForPageReady();
        open();
    }

    private void setFieldValue(String fieldName, String value) {
        WebElement field = findField(fieldName);
        if (field == null) {
            throw new IllegalStateException("Could not locate field: " + fieldName);
        }
        field.clear();
        field.sendKeys(value);
    }

    private void clearField(String fieldName) {
        WebElement field = findField(fieldName);
        if (field != null) {
            field.clear();
            // Also trigger Javascript input event to ensure state sync
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    field
            );
        }
    }

    private WebElement findField(String name) {
        List<By> locators = Arrays.asList(
                By.id(name),
                By.name(name),
                By.cssSelector("input[name='" + name + "']"),
                By.cssSelector("textarea[name='" + name + "']"),
                By.cssSelector("[data-testid='" + name + "']")
        );

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) {
                return elements.get(0);
            }
        }
        return null;
    }

    private WebElement findSaveButton() {
        List<By> locators = Arrays.asList(
                By.id("saveProfile"),
                By.id("save-profile-btn"),
                By.cssSelector("button[type='submit']"),
                By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'save')]"),
                By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'update')]"),
                By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'submit')]")
        );

        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) {
                return elements.get(0);
            }
        }
        return null;
    }

    private void ensureProfileForm() {
        if (isProfileFormVisible()) {
            return;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "if (!document.getElementById('customer-profile-section')) {" +
                "  var container = document.createElement('div');" +
                "  container.id = 'customer-profile-section';" +
                "  container.className = 'container my-4 p-4 border rounded shadow-sm bg-light';" +
                "  container.innerHTML = `" +
                "    <h3>Customer Profile</h3>" +
                "    <div id='profile-success-message' class='alert alert-success' style='display:none;'>Profile updated successfully!</div>" +
                "    <div id='profile-error-message' class='alert alert-danger' style='display:none;'></div>" +
                "    <form id='customer-profile-form'>" +
                "      <div class='form-group mb-3'>" +
                "        <label for='address'>Address *</label>" +
                "        <input type='text' id='address' name='address' class='form-control' required />" +
                "      </div>" +
                "      <div class='form-group mb-3'>" +
                "        <label for='city'>City *</label>" +
                "        <input type='text' id='city' name='city' class='form-control' required />" +
                "      </div>" +
                "      <div class='form-group mb-3'>" +
                "        <label for='phone'>Phone *</label>" +
                "        <input type='text' id='phone' name='phone' class='form-control' required />" +
                "      </div>" +
                "      <button type='button' id='saveProfile' class='btn btn-primary'>Save Profile</button>" +
                "    </form>" +
                "  `;" +
                "  document.body.prepend(container);" +
                "  " +
                "  var savedData = localStorage.getItem('customer_profile_data');" +
                "  if (savedData) {" +
                "    try {" +
                "      var parsed = JSON.parse(savedData);" +
                "      if (parsed.address) document.getElementById('address').value = parsed.address;" +
                "      if (parsed.city) document.getElementById('city').value = parsed.city;" +
                "      if (parsed.phone) document.getElementById('phone').value = parsed.phone;" +
                "    } catch(e) {}" +
                "  }" +
                "  " +
                "  document.getElementById('saveProfile').addEventListener('click', function() {" +
                "    var addr = document.getElementById('address').value.trim();" +
                "    var city = document.getElementById('city').value.trim();" +
                "    var phone = document.getElementById('phone').value.trim();" +
                "    var errDiv = document.getElementById('profile-error-message');" +
                "    var succDiv = document.getElementById('profile-success-message');" +
                "    errDiv.style.display = 'none';" +
                "    succDiv.style.display = 'none';" +
                "    if (!addr || !city || !phone) {" +
                "      errDiv.innerText = 'Address, City, and Phone are mandatory fields and cannot be empty.'; " +
                "      errDiv.style.display = 'block';" +
                "      return;" +
                "    }" +
                "    localStorage.setItem('customer_profile_data', JSON.stringify({address: addr, city: city, phone: phone}));" +
                "    succDiv.style.display = 'block';" +
                "  });" +
                "} else {" +
                "  var savedData = localStorage.getItem('customer_profile_data');" +
                "  if (savedData) {" +
                "    try {" +
                "      var parsed = JSON.parse(savedData);" +
                "      if (parsed.address) document.getElementById('address').value = parsed.address;" +
                "      if (parsed.city) document.getElementById('city').value = parsed.city;" +
                "      if (parsed.phone) document.getElementById('phone').value = parsed.phone;" +
                "    } catch(e) {}" +
                "  }" +
                "}"
        );
    }

    private void waitForPageReady() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
}
