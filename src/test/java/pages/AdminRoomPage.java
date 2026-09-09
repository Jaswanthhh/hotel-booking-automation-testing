package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminRoomPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Room form locators
    private By roomName = By.id("roomName");
    private By roomType = By.id("type");
    private By accessible = By.id("accessible");
    private By roomPrice = By.id("roomPrice");

    // Room feature locators
    private By wifiCheckbox = By.id("wifiCheckbox");
    private By tvCheckbox = By.id("tvCheckbox");
    private By radioCheckbox = By.id("radioCheckbox");
    private By refreshCheckbox = By.id("refreshCheckbox");
    private By safeCheckbox = By.id("safeCheckbox");
    private By viewsCheckbox = By.id("viewsCheckbox");

    // Create button
    private By createRoomButton = By.id("createRoom");

    public AdminRoomPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterRoomNumber(String roomNumber) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(roomName))
                .sendKeys(roomNumber);
    }

    public void selectRoomType(String type) {

        Select select = new Select(
                wait.until(ExpectedConditions.visibilityOfElementLocated(roomType))
        );

        select.selectByVisibleText(type);
    }

    public void selectAccessible(String value) {

        Select select = new Select(
                wait.until(ExpectedConditions.visibilityOfElementLocated(accessible))
        );

        select.selectByVisibleText(value);
    }

    public void enterRoomPrice(String price) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(roomPrice))
                .sendKeys(price);
    }

    public void selectWiFi() {

        wait.until(ExpectedConditions.elementToBeClickable(wifiCheckbox))
                .click();
    }

    public void selectTV() {

        wait.until(ExpectedConditions.elementToBeClickable(tvCheckbox))
                .click();
    }

    public void selectRadio() {

        wait.until(ExpectedConditions.elementToBeClickable(radioCheckbox))
                .click();
    }

    public void selectRefreshments() {

        wait.until(ExpectedConditions.elementToBeClickable(refreshCheckbox))
                .click();
    }

    public void selectSafe() {

        wait.until(ExpectedConditions.elementToBeClickable(safeCheckbox))
                .click();
    }

    public void selectViews() {

        wait.until(ExpectedConditions.elementToBeClickable(viewsCheckbox))
                .click();
    }

    public void clickCreateRoom() {

        wait.until(ExpectedConditions.elementToBeClickable(createRoomButton))
                .click();
    }

    public boolean isRoomDisplayed(String roomNumber) {

        By room = By.id("roomName" + roomNumber);

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(room)
        ).isDisplayed();
    }
}