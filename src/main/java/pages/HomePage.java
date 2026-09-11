package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String URL =
            "https://automationintesting.online/";

    // -----------------------------
    // Locators
    // -----------------------------

    private final By navigationBar =
            By.cssSelector("nav.navbar");

    private final By logo =
            By.cssSelector("a.navbar-brand");

    /*
     * Use text instead of the exact href.
     * This is more robust if the website changes
     * /#rooms to another equivalent URL format.
     */
    private final By roomsLink =
            By.xpath("//nav//a[contains(normalize-space(), 'Rooms')]");

    private final By contactLink =
            By.xpath("//nav//a[contains(normalize-space(), 'Contact')]");

    private final By homeLink =
            By.cssSelector("a.navbar-brand");

    private final By roomsSection =
            By.id("rooms");

    private final By contactSection =
            By.id("contact");

    private final By footer =
            By.tagName("footer");

    private final By images =
            By.tagName("img");

    private final By footerLinks =
            By.cssSelector("footer a");


    // -----------------------------
    // Constructor
    // -----------------------------

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }


    // -----------------------------
    // Open Website
    // -----------------------------

    public void openWebsite() {

        driver.get(URL);

        wait.until(driver ->
                driver.getCurrentUrl()
                        .contains("automationintesting.online")
        );

        // Wait until the navigation is visible
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        navigationBar
                )
        );
    }


    // -----------------------------
    // Page Information
    // -----------------------------

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }


    // -----------------------------
    // Navigation
    // -----------------------------

    public boolean isNavigationDisplayed() {
        return isDisplayed(navigationBar);
    }

    public boolean isHomeLinkDisplayed() {
        return isDisplayed(homeLink);
    }

    public boolean isRoomsLinkDisplayed() {
        return isDisplayed(roomsLink);
    }

    public boolean isContactLinkDisplayed() {
        return isDisplayed(contactLink);
    }


    // -----------------------------
    // Navigation Clicks
    // -----------------------------

    public void clickHome() {

        WebElement home = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        homeLink
                )
        );

        scrollElementIntoView(home);

        wait.until(
                ExpectedConditions.elementToBeClickable(home)
        );

        home.click();
    }


    public void clickRooms() {

        WebElement rooms = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        roomsLink
                )
        );

        /*
         * Move the Rooms link to the center of the viewport
         * before clicking.
         *
         * This helps avoid sticky headers or other elements
         * intercepting the click.
         */
        scrollElementIntoView(rooms);

        wait.until(
                ExpectedConditions.elementToBeClickable(rooms)
        );

        rooms.click();
    }


    public void clickContact() {

        WebElement contact = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        contactLink
                )
        );

        /*
         * Move Contact link to the center of the viewport
         * before clicking.
         */
        scrollElementIntoView(contact);

        wait.until(
                ExpectedConditions.elementToBeClickable(contact)
        );

        contact.click();
    }


    // -----------------------------
    // Logo
    // -----------------------------

    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }


    // -----------------------------
    // Rooms Section
    // -----------------------------

    public boolean isRoomsSectionDisplayed() {
        return isDisplayed(roomsSection);
    }


    // -----------------------------
    // Contact Section
    // -----------------------------

    public boolean isContactSectionDisplayed() {
        return isDisplayed(contactSection);
    }


    // -----------------------------
    // Images
    // -----------------------------

    public List<WebElement> getImages() {
        return driver.findElements(images);
    }

    public int getImageCount() {
        return getImages().size();
    }


    // -----------------------------
    // Footer
    // -----------------------------

    public boolean isFooterDisplayed() {
        return isDisplayed(footer);
    }

    public List<WebElement> getFooterLinks() {
        return driver.findElements(footerLinks);
    }


    public void scrollToFooter() {

        WebElement footerElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        footer
                )
        );

        scrollElementIntoView(footerElement);
    }


    // -----------------------------
    // Scroll to Rooms
    // -----------------------------

    public void scrollToRooms() {

        WebElement roomsElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        roomsSection
                )
        );

        scrollElementIntoView(roomsElement);
    }


    // -----------------------------
    // Scroll to Contact
    // -----------------------------

    public void scrollToContact() {

        WebElement contactElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        contactSection
                )
        );

        scrollElementIntoView(contactElement);
    }


    // -----------------------------
    // Helper - Scroll Element
    // -----------------------------

    private void scrollElementIntoView(WebElement element) {

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                element
        );

        /*
         * Small pause allows the browser to finish scrolling
         * and any sticky navigation animation to settle.
         */
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    // -----------------------------
    // Helper - Check Displayed
    // -----------------------------

    private boolean isDisplayed(By locator) {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            locator
                    )
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }
}
