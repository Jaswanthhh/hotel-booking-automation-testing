package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By username = By.id("username");
    private By password = By.id("password");
    private By loginButton = By.id("doLogin");

    public LoginPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterUsername(String usernameValue) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(username))
                .sendKeys(usernameValue);
    }

    public void enterPassword(String passwordValue) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(password))
                .sendKeys(passwordValue);
    }

    public void clickLogin() {

        wait.until(ExpectedConditions.elementToBeClickable(loginButton))
                .click();
    }

    public void login(String usernameValue, String passwordValue) {

        enterUsername(usernameValue);
        enterPassword(passwordValue);
        clickLogin();
    }
}