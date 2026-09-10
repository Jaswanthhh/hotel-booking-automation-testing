package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import utils.ConfigReader;

import java.util.List;

public class CustomerBookingPage extends BasePage {

    private final By bookThisRoomButton = By.xpath("//button[text()='Book this room' or contains(@class, 'openBooking') or contains(text(), 'Book')]");
    private final By firstNameInput = By.name("firstname");
    private final By lastNameInput = By.name("lastname");
    private final By emailInput = By.name("email");
    private final By phoneInput = By.name("phone");
    private final By bookSubmitButton = By.xpath("//button[text()='Book' and contains(@class, 'btn-outline-primary')]");
    private final By confirmationModal = By.xpath("//div[contains(@class, 'confirmation-modal') or contains(., 'Booking Successful!')]");
    private final By bookingCalendarCells = By.cssSelector(".rbc-month-view .rbc-day-bg");
    private final By todayBtn = By.xpath("//button[text()='Today']");
    private final By nextBtn = By.xpath("//button[text()='Next']");

    public CustomerBookingPage(WebDriver driver) {
        super(driver);
    }

    public CustomerBookingPage open() {
        String url = ConfigReader.getProperty("app.url");
        driver.get(url);
        return this;
    }

    public CustomerBookingPage clickBookThisRoom() {
        try {
            List<WebElement> buttons = driver.findElements(By.xpath("//button[contains(@class, 'openBooking') or text()='Book this room' or contains(text(), 'Book')]"));
            if (!buttons.isEmpty()) {
                WebElement btn = buttons.get(0);
                waitUtils.scrollToElement(btn);
                waitUtils.clickViaJs(btn);
            }
        } catch (Exception e) {
            // fallback
        }
        return this;
    }

    public CustomerBookingPage selectDatesOnCalendar() {
        try {
            List<WebElement> days = waitUtils.waitForAllVisible(bookingCalendarCells);
            if (days.size() >= 15) {
                WebElement startDay = days.get(10);
                WebElement endDay = days.get(12);
                Actions actions = new Actions(driver);
                actions.moveToElement(startDay).clickAndHold().moveToElement(endDay).release().build().perform();
            }
        } catch (Exception e) {
            // Calendar drag attempt completed
        }
        return this;
    }

    public CustomerBookingPage fillGuestDetails(String fName, String lName, String email, String phone) {
        WebElement fn = waitUtils.waitForVisibility(firstNameInput);
        fn.clear();
        fn.sendKeys(fName);

        WebElement ln = driver.findElement(lastNameInput);
        ln.clear();
        ln.sendKeys(lName);

        WebElement em = driver.findElement(emailInput);
        em.clear();
        em.sendKeys(email);

        WebElement ph = driver.findElement(phoneInput);
        ph.clear();
        ph.sendKeys(phone);

        return this;
    }

    public CustomerBookingPage submitBooking() {
        WebElement submitBtn = waitUtils.waitForClickable(bookSubmitButton);
        submitBtn.click();
        return this;
    }

    public boolean isBookingSuccessful() {
        try {
            WebElement modal = waitUtils.waitForVisibility(confirmationModal);
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
