package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;

public class AdminBookingPage extends BasePage {

    private final By reportNavLink = By.xpath("//a[contains(@href, '/admin/report') or text()='Report' or contains(text(), 'Report')]");
    private final By bookingsCalendar = By.cssSelector(".rbc-calendar");
    private final By calendarToolbarLabel = By.cssSelector(".rbc-toolbar-label");
    private final By calendarNextButton = By.xpath("//button[text()='Next']");
    private final By calendarBackButton = By.xpath("//button[text()='Back']");
    private final By calendarTodayButton = By.xpath("//button[text()='Today']");
    private final By bookingEvents = By.cssSelector(".rbc-event, .rbc-event-content");
    private final By roomListings = By.cssSelector("[data-testid='roomlisting']");

    public AdminBookingPage(WebDriver driver) {
        super(driver);
    }

    public AdminBookingPage openReport() {
        try {
            // First try clicking the Report navigation link if present
            WebElement reportNav = waitUtils.waitForClickable(reportNavLink);
            waitUtils.clickViaJs(reportNav);
        } catch (Exception e) {
            // If nav link is not clickable, navigate directly to report URL
            driver.get(ConfigReader.getProperty("admin.report.url"));
        }
        // Wait for calendar or report container to render
        try {
            waitUtils.waitForVisibility(bookingsCalendar);
        } catch (Exception ignored) {}
        return this;
    }

    public AdminBookingPage clickReportNav() {
        try {
            WebElement reportLink = waitUtils.waitForClickable(reportNavLink);
            reportLink.click();
        } catch (Exception e) {
            openReport();
        }
        return this;
    }

    public boolean isCalendarDisplayed() {
        try {
            WebElement calendar = waitUtils.waitForVisibility(bookingsCalendar);
            return calendar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCalendarLabel() {
        try {
            WebElement label = waitUtils.waitForVisibility(calendarToolbarLabel);
            return label.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickNextMonth() {
        WebElement next = waitUtils.waitForClickable(calendarNextButton);
        next.click();
    }

    public void clickBackMonth() {
        WebElement back = waitUtils.waitForClickable(calendarBackButton);
        back.click();
    }

    public void clickToday() {
        WebElement today = waitUtils.waitForClickable(calendarTodayButton);
        today.click();
    }

    public List<String> getAllBookingEventTexts() {
        List<String> eventTexts = new ArrayList<>();
        try {
            List<WebElement> events = driver.findElements(bookingEvents);
            for (WebElement ev : events) {
                String text = ev.getText().trim();
                if (!text.isEmpty() && !eventTexts.contains(text)) {
                    eventTexts.add(text);
                }
            }
        } catch (Exception e) {
            // return current list
        }
        return eventTexts;
    }

    public boolean isBookingPresent(String guestName) {
        List<String> events = getAllBookingEventTexts();
        for (String ev : events) {
            if (ev.toLowerCase().contains(guestName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public int getRoomListingCount() {
        try {
            List<WebElement> rooms = driver.findElements(roomListings);
            return rooms.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
