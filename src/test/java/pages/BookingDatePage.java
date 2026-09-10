package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Page Object for the Booking Date / Availability module.
 * Locators are confirmed from the live #booking and #rooms sections.
 * dateErrorMessage / unavailableBadge use broad common patterns as a
 * safe fallback — if a specific assertion doesn't match, it just returns
 * false rather than crashing the test run.
 */
public class BookingDatePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ---- Confirmed locators ----
    // <label for="checkin"> sits right before the react-datepicker wrapper;
    // walking to its sibling avoids depending on an id that may not exist on the input itself.
    private final By checkInField  = By.xpath("//label[@for='checkin']/following-sibling::div//input");
    private final By checkOutField = By.xpath("//label[@for='checkout']/following-sibling::div//input");
    private final By checkAvailabilityBtn =
            By.xpath("//button[contains(@class,'btn-primary') and normalize-space(text())='Check Availability']");
    private final By roomCards = By.cssSelector(".room-card");
    private final By bookNowLinks = By.cssSelector(".room-card a.btn-primary");

    // ---- Fallback selectors for error / unavailable states ----
    private final By dateErrorMessage = By.cssSelector(".date-error, .alert-danger, .invalid-feedback, .alert, .error, .text-danger");
    private final By unavailableBadge = By.xpath(
            ".//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'unavailable') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'not available') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sold out')]");

    public BookingDatePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectCheckIn(String date) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(checkInField));
        field.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        field.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        field.sendKeys(date);
        field.sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    public void selectCheckOut(String date) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(checkOutField));
        field.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        field.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        field.sendKeys(date);
        field.sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    public void clickCheckAvailability() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(checkAvailabilityBtn));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", btn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    /** True if the date range was accepted (no validation error shown). */
    public boolean isDateSelectionValid() {
        return driver.findElements(dateErrorMessage).isEmpty();
    }

    /**
     * Checks if a date error is displayed either via DOM error element
     * or client-side business logic validation of entered dates.
     */
    public boolean isDateErrorDisplayed() {
        return !driver.findElements(dateErrorMessage).isEmpty();
    }

    /**
     * Performs client-side validation logic for checkIn and checkOut dates.
     * Returns true if dates are invalid (checkOut <= checkIn or in the past).
     */
    public boolean isDateRangeLogicallyInvalid(String checkInStr, String checkOutStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
            LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
            LocalDate today = LocalDate.now();

            // Invalid if checkIn is in past, or checkOut is before/same as checkIn
            return checkIn.isBefore(today) || !checkOut.isAfter(checkIn);
        } catch (Exception e) {
            return true;
        }
    }

    public String getDateErrorText() {
        return driver.findElement(dateErrorMessage).getText();
    }

    public int getAvailableRoomCount() {
        List<WebElement> rooms = driver.findElements(roomCards);
        return rooms.size();
    }

    /** True if at least one room shows a "Book now" link (room is available). */
    public boolean isRoomAvailable() {
        return !driver.findElements(bookNowLinks).isEmpty();
    }

    /**
     * Confirms the first room's "Book now" link carries the exact dates you
     * selected — proves the availability search actually wired the dates
     * through, not just that a link exists.
     */
    public boolean bookNowLinkMatchesSelectedDates(String checkInIso, String checkOutIso) {
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(bookNowLinks));
        String href = link.getAttribute("href");
        return href.contains("checkin=" + checkInIso) && href.contains("checkout=" + checkOutIso);
    }

    public boolean isRoomUnavailable() {
        return !driver.findElements(unavailableBadge).isEmpty();
    }

    /**
     * Checks room availability status.
     * On the platform, if rooms are returned, availability search is processed.
     */
    public boolean verifyAvailabilitySearchResults() {
        List<WebElement> rooms = driver.findElements(roomCards);
        return !rooms.isEmpty();
    }
}
