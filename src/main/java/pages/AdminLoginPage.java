package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;

public class AdminLoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("doLogin");
    private final By logoutButton = By.xpath("//a[text()='Logout' or contains(@href, 'logout')] | //button[text()='Logout']");
    private final By frontPageHeader = By.xpath("//a[contains(@class, 'navbar-brand') or contains(text(), 'B&B')]");

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    public AdminLoginPage open() {
        driver.get(ConfigReader.getProperty("admin.login.url"));
        return this;
    }

    public AdminLoginPage enterUsername(String username) {
        WebElement user = waitUtils.waitForVisibility(usernameField);
        user.clear();
        user.sendKeys(username);
        return this;
    }

    public AdminLoginPage enterPassword(String password) {
        WebElement pass = waitUtils.waitForVisibility(passwordField);
        pass.clear();
        pass.sendKeys(password);
        return this;
    }

    public AdminLoginPage clickLogin() {
        WebElement loginBtn = waitUtils.waitForClickable(loginButton);
        loginBtn.click();
        return this;
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void loginWithDefaultAdmin() {
        String user = ConfigReader.getProperty("admin.username");
        String pass = ConfigReader.getProperty("admin.password");
        login(user, pass);
    }

    public boolean isLoggedIn() {
        try {
            waitUtils.waitForInvisibility(loginButton);
            return waitUtils.waitForVisibility(logoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
