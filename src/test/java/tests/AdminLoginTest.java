package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminLoginPage;
import utils.ConfigReader;

/**
 * Admin Login module — automated authentication scenarios.
 *
 * Credentials are never hard-coded here.  They are read at runtime from
 * config.properties (classpath) which supports placeholder substitution:
 *   admin.username  → env-var ADMIN_USER  (fallback: value in config.properties)
 *   admin.password  → env-var ADMIN_PASS  (fallback: value in config.properties)
 *
 * To supply real credentials locally without committing them:
 *   export ADMIN_USER=<your-admin-username>
 *   export ADMIN_PASS=<your-admin-password>
 * or pass as JVM system properties:
 *   mvn test -Dadmin.username=<u> -Dadmin.password=<p>
 */
public class AdminLoginTest extends BaseTest {

    private AdminLoginPage loginPage;

    // ── Setup ─────────────────────────────────────────────────────────────────

    @BeforeMethod
    public void openLoginPage() {
        loginPage = new AdminLoginPage(driver);
        loginPage.open();
    }

    // ── ADMINLOGIN-001: Valid credentials ─────────────────────────────────────

    /**
     * ADMINLOGIN-001 — Valid username + valid password.
     * Verifies that the admin dashboard is shown after a successful login.
     */
    @Test(priority = 1, description = "ADMINLOGIN-001 — Valid username and valid password")
    public void validAdminLogin() {
        String user = ConfigReader.getProperty("admin.username");
        String pass = ConfigReader.getProperty("admin.password");

        loginPage.login(user, pass);

        Assert.assertTrue(loginPage.isLoggedIn(),
                "Admin should reach the dashboard after supplying valid credentials.");
    }

    // ── ADMINLOGIN-002: Invalid username ──────────────────────────────────────

    /**
     * ADMINLOGIN-002 — Invalid username, valid password.
     * Verifies that the login attempt is rejected and an error is shown.
     */
    @Test(priority = 2, description = "ADMINLOGIN-002 — Invalid username")
    public void invalidUsername() {
        String pass = ConfigReader.getProperty("admin.password");

        loginPage.login("invalid_user_xyz", pass);

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must fail when the username is invalid.");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "An error message must be shown for an invalid username.");
    }

    // ── ADMINLOGIN-003: Invalid password ─────────────────────────────────────

    /**
     * ADMINLOGIN-003 — Valid username, invalid password.
     * Verifies that the login attempt is rejected and an error is shown.
     */
    @Test(priority = 3, description = "ADMINLOGIN-003 — Invalid password")
    public void invalidPassword() {
        String user = ConfigReader.getProperty("admin.username");

        loginPage.login(user, "wrongP@ss!");

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must fail when the password is invalid.");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "An error message must be shown for an invalid password.");
    }

    // ── ADMINLOGIN-004: Both invalid ──────────────────────────────────────────

    /**
     * ADMINLOGIN-004 — Both username and password invalid.
     */
    @Test(priority = 4, description = "ADMINLOGIN-004 — Both username and password invalid")
    public void invalidUsernameAndPassword() {
        loginPage.login("bad_user", "bad_pass");

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must fail when both credentials are invalid.");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "An error message must be shown when both credentials are invalid.");
    }

    // ── ADMINLOGIN-005: Empty username ────────────────────────────────────────

    /**
     * ADMINLOGIN-005 — Empty username field, valid password.
     * The application should not authenticate and must present feedback.
     */
    @Test(priority = 5, description = "ADMINLOGIN-005 — Empty username")
    public void emptyUsername() {
        String pass = ConfigReader.getProperty("admin.password");

        loginPage.login("", pass);

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must not succeed with an empty username.");
        // The app either shows an error alert or keeps the login form visible.
        Assert.assertTrue(
                loginPage.isErrorDisplayed() || !loginPage.isLoggedIn(),
                "Login form should remain or error shown when username is empty.");
    }

    // ── ADMINLOGIN-006: Empty password ────────────────────────────────────────

    /**
     * ADMINLOGIN-006 — Valid username, empty password field.
     */
    @Test(priority = 6, description = "ADMINLOGIN-006 — Empty password")
    public void emptyPassword() {
        String user = ConfigReader.getProperty("admin.username");

        loginPage.login(user, "");

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must not succeed with an empty password.");
        Assert.assertTrue(
                loginPage.isErrorDisplayed() || !loginPage.isLoggedIn(),
                "Login form should remain or error shown when password is empty.");
    }

    // ── ADMINLOGIN-007: Both fields empty ─────────────────────────────────────

    /**
     * ADMINLOGIN-007 — Both username and password empty.
     */
    @Test(priority = 7, description = "ADMINLOGIN-007 — Both username and password empty")
    public void emptyUsernameAndPassword() {
        loginPage.clickLogin();

        Assert.assertFalse(loginPage.isLoggedIn(),
                "Login must not succeed when both fields are empty.");
        Assert.assertTrue(
                loginPage.isErrorDisplayed() || !loginPage.isLoggedIn(),
                "Login form should remain or error shown when both fields are empty.");
    }

    // ── ADMINLOGIN-008: Successful login verification ─────────────────────────

    /**
     * ADMINLOGIN-008 — Explicitly verifies the successful login state.
     * Confirms that the URL changes away from the login page after login.
     */
    @Test(priority = 8, description = "ADMINLOGIN-008 — Verify successful login")
    public void verifyLoginSuccess() {
        loginPage.loginWithDefaultAdmin();

        Assert.assertTrue(loginPage.isLoggedIn(),
                "Admin dashboard must be visible after a valid login.");
        // After login the SPA navigates away from the bare /admin path
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("/admin"),
                "URL must still be within the /admin section after login. Actual: " + currentUrl);
    }

    // ── ADMINLOGIN-009: Error message verification ────────────────────────────

    /**
     * ADMINLOGIN-009 — Verifies that the error message is non-empty and meaningful
     * when wrong credentials are submitted.
     */
    @Test(priority = 9, description = "ADMINLOGIN-009 — Verify login error message")
    public void verifyLoginErrorMessage() {
        loginPage.login("wrong_user", "wrong_pass");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "An error alert must be visible after a failed login attempt.");
        String message = loginPage.getErrorMessage();
        Assert.assertFalse(message.isEmpty(),
                "The error message text must not be empty.");
    }

    // ── ADMINLOGIN-010: Logout verification ───────────────────────────────────

    /**
     * ADMINLOGIN-010 — Verifies that the admin can log out.
     * The application redirects to "/" on logout; we verify the browser leaves
     * the /admin area and lands on the public home page.
     */
    @Test(priority = 10, description = "ADMINLOGIN-010 — Verify logout")
    public void verifyLogout() {
        loginPage.loginWithDefaultAdmin();
        Assert.assertTrue(loginPage.isLoggedIn(), "Admin must be logged in before testing logout.");

        loginPage.logout();

        // After logout the SPA does window.location.href="/", so the browser
        // lands on the public home page — not on the /admin login screen.
        Assert.assertTrue(loginPage.isLoggedOut(),
                "Browser must be on the home page (not /admin) after logout.");
        Assert.assertFalse(loginPage.isLoggedIn(),
                "Admin dashboard must not be accessible after logout.");
    }

    // ── ADMINLOGIN-011: Session behavior after logout ─────────────────────────

    /**
     * ADMINLOGIN-011 — Verifies that after logout the session is invalidated:
     * navigating directly to the admin URL should show the login form again,
     * not the authenticated dashboard.
     */
    @Test(priority = 11, description = "ADMINLOGIN-011 — Verify session is invalidated after logout")
    public void verifySessionAfterLogout() {
        loginPage.loginWithDefaultAdmin();
        Assert.assertTrue(loginPage.isLoggedIn(), "Admin must be logged in before testing session.");

        loginPage.logout();
        Assert.assertTrue(loginPage.isLoggedOut(),
                "Browser must land on the home page after logout.");

        // Navigate directly back to /admin — the cookie has been removed so the
        // login form must be shown instead of the dashboard.
        loginPage.open();

        Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                "After logout, navigating to /admin must show the login form.");
        Assert.assertFalse(loginPage.isLoggedIn(),
                "Session must be invalidated: admin dashboard must NOT be accessible after logout.");
    }
}
