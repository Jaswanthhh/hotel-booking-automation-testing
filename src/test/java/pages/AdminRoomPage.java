package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class AdminRoomPage {

    private WebDriver driver;

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

    // Constructor
    public AdminRoomPage(WebDriver driver) {
        this.driver = driver;
    }

    // Enter room number
    public void enterRoomNumber(String roomNumber) {
        driver.findElement(roomName).sendKeys(roomNumber);
    }

    // Select room type
    public void selectRoomType(String type) {
        Select select = new Select(driver.findElement(roomType));
        select.selectByVisibleText(type);
    }

    // Select accessibility
    public void selectAccessible(String value) {
        Select select = new Select(driver.findElement(accessible));
        select.selectByVisibleText(value);
    }

    // Enter room price
    public void enterRoomPrice(String price) {
        driver.findElement(roomPrice).sendKeys(price);
    }

    // Select WiFi
    public void selectWiFi() {
        driver.findElement(wifiCheckbox).click();
    }

    // Select TV
    public void selectTV() {
        driver.findElement(tvCheckbox).click();
    }

    // Select Radio
    public void selectRadio() {
        driver.findElement(radioCheckbox).click();
    }

    // Select Refreshments
    public void selectRefreshments() {
        driver.findElement(refreshCheckbox).click();
    }

    // Select Safe
    public void selectSafe() {
        driver.findElement(safeCheckbox).click();
    }

    // Select Views
    public void selectViews() {
        driver.findElement(viewsCheckbox).click();
    }

    // Create room
    public void clickCreateRoom() {
        driver.findElement(createRoomButton).click();
    }
}