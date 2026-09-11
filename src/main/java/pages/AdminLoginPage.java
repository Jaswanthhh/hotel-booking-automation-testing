package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;

public class AdminLoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("doLogin");
    // Button rendered by the admin navbar: <button class="btn btn-outline-danger ...">Logout</button>
    private final By logoutButton  = By.cssSelector("button.btn-outline-danger");
    // Bootstrap alert shown on bad credentials
    private final By errorMessage  = By.cssSelector(".alert-danger");
    // Booking section present on the public home page "/" — used to confirm post-logout redirect
    private final By bookingSection = By.id("booking");

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public AdminLoginPage open() {
        driver.get(ConfigReader.getProperty("admin.login.url"));
        return this;
    }

    // ── Field interactions ────────────────────────────────────────────────────

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

    // ── Compound actions ──────────────────────────────────────────────────────

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /** Reads credentials from config.properties (or env-vars ADMIN_USER / ADMIN_PASS). */
    public void loginWithDefaultAdmin() {
        String user = ConfigReader.getProperty("admin.username");
        String pass = ConfigReader.getProperty("admin.password");
        login(user, pass);
    }

    // ── State queries ─────────────────────────────────────────────────────────

    /** Returns true when the logout button is visible (i.e. admin dashboard is shown). */
    public boolean isLoggedIn() {
        try {
            waitUtils.waitForInvisibility(loginButton);
            return waitUtils.waitForVisibility(logoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the error alert is visible (used for failed-login assertions). */
    public boolean isErrorDisplayed() {
        try {
            return waitUtils.waitForVisibility(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the trimmed text of the error alert, or an empty string when no
     * alert is present.  Never exposes credentials in log output.
     */
    public String getErrorMessage() {
        try {
            return waitUtils.waitForVisibility(errorMessage).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Clicks the logout button and waits for the browser to land on the public
     * home page.  The admin SPA executes window.location.href="/" after logout,
     * so we wait for the booking section (id="booking") that is only rendered
     * on the home page.
     */
    public void logout() {
        waitUtils.waitForClickable(logoutButton).click();
        // Block until the post-logout redirect to "/" completes
        waitUtils.waitForVisibility(bookingSection);
    }

    /**
     * Returns true when the browser is on the public home page (not /admin).
     * Call this after logout() — by that point the redirect has already finished.
     */
    public boolean isLoggedOut() {
        try {
            String url = driver.getCurrentUrl();
            return !url.contains("/admin") && driver.findElement(bookingSection).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true when navigating to /admin shows the login form.
     * Use this after logout() + open() to verify session invalidation.
     */
    public boolean isLoginFormDisplayed() {
        try {
            return waitUtils.waitForVisibility(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
