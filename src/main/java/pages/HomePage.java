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
    private static final String URL = "https://automationintesting.online/";

    private final By navigationBar = By.cssSelector("nav.navbar");
    private final By logo = By.cssSelector("a.navbar-brand");
    private final By roomsLink = By.cssSelector("a.nav-link[href='/#rooms']");
    private final By contactLink = By.cssSelector("a.nav-link[href='/#contact']");
    private final By homeLink = By.cssSelector("a.navbar-brand");
    private final By roomsSection = By.id("rooms");
    private final By contactSection = By.id("contact");
    private final By footer = By.tagName("footer");
    private final By images = By.tagName("img");
    private final By footerLinks = By.cssSelector("footer a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openWebsite() {
        driver.get(URL);
        wait.until(driver -> driver.getCurrentUrl().contains("automationintesting.online"));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

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

    public void clickHome() {
        clickable(homeLink).click();
    }

    public void clickRooms() {
        clickable(roomsLink).click();
    }

    public void clickContact() {
        clickable(contactLink).click();
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }

    public boolean isRoomsSectionDisplayed() {
        return isDisplayed(roomsSection);
    }

    public boolean isContactSectionDisplayed() {
        return isDisplayed(contactSection);
    }

    public List<WebElement> getImages() {
        return driver.findElements(images);
    }

    public int getImageCount() {
        return getImages().size();
    }

    public boolean isFooterDisplayed() {
        return isDisplayed(footer);
    }

    public List<WebElement> getFooterLinks() {
        return driver.findElements(footerLinks);
    }

    public void scrollToFooter() {
        WebElement footerElement = wait.until(ExpectedConditions.presenceOfElementLocated(footer));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", footerElement);
    }

    public void scrollToRooms() {
        WebElement roomsElement = wait.until(ExpectedConditions.presenceOfElementLocated(roomsSection));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", roomsElement);
    }

    public void scrollToContact() {
        WebElement contactElement = wait.until(ExpectedConditions.presenceOfElementLocated(contactSection));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", contactElement);
    }

    private boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}