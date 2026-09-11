import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class singleRoomBooking {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeTest
    public void initial(){
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://automationintesting.online/");
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void validTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("Firstname");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("valid@email.com");
        phone.clear();
        phone.sendKeys("012345678901");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            WebElement confirmationCard = confirmationCards.get(0);

            // Assert confirmation details
            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            // Proceed: Return to homepage
            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(".//a[normalize-space()='Return home']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // Confirmation card did not appear; verify error alert or fail test
            WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-danger")));
            Assert.fail("Booking was not confirmed. Error visible: " + errorAlert.getText());
        }
    }

    @Test(priority = 2)
    public void missingFirstNameTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("valid@email.com");
        phone.clear();
        phone.sendKeys("012345678901");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Firstname should not be blank"),
                    "Expected 'Firstname should not be blank' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @Test(priority = 3)
    public void missingLastNameTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("Firstname");
        lname.clear();
        lname.sendKeys("");
        email.clear();
        email.sendKeys("valid@email.com");
        phone.clear();
        phone.sendKeys("012345678901");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Lastname should not be blank"),
                    "Expected 'Lastname should not be blank' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @Test(priority = 4)
    public void missingEmailTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("Firstname");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("");
        phone.clear();
        phone.sendKeys("012345678901");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Email should not be blank"),
                    "Expected 'Email should not be blank' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @Test(priority = 5)
    public void invalidEmailTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("invalidemail.com");
        phone.clear();
        phone.sendKeys("012345678901");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Invalid email provided"),
                    "Expected 'Invalid email provided' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @Test(priority = 6)
    public void missingPhoneTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("Firstname");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("invalidemail.com");
        phone.clear();
        phone.sendKeys("");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Phone number missing"),
                    "Expected 'Phone number missing' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @Test(priority = 7)
    public void invalidPhoneTest(){
        WebElement book = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.btn-primary[href*='/reservation/1']")
        ));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", book);
        js.executeScript("arguments[0].click();", book);

        // 1. Wait for the calendar grid days to be visible
        List<WebElement> days = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".rbc-day-bg:not(.rbc-off-range-bg)")
        ));
        // 2. Select two available future dates via drag-and-drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(days.get(12), days.get(14)).perform();

        WebElement reserveNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("doReservation")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reserveNow);
        js.executeScript("arguments[0].click();", reserveNow);

        WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname")));
        WebElement lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname")));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phone")));
        fname.clear();
        fname.sendKeys("Firstname");
        lname.clear();
        lname.sendKeys("Lastname");
        email.clear();
        email.sendKeys("invalidemail.com");
        phone.clear();
        phone.sendKeys("12345");

        WebElement reserveSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Reserve Now']")
        ));
        js.executeScript("arguments[0].click();", reserveSubmit);

        // Locate both potential outcome containers
        List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
        List<WebElement> confirmationCards = driver.findElements(By.cssSelector(".booking-card"));

        if (!errorAlerts.isEmpty() && errorAlerts.get(0).isDisplayed()) {
            // 1. Error Alert Branch (Validation Failed)
            WebElement alert = errorAlerts.get(0);

            // Extract all error list items (<li>)
            List<WebElement> errorList = alert.findElements(By.tagName("li"));

            // Convert errors to a readable list or string for assertion
            StringBuilder errorsText = new StringBuilder();
            for (WebElement error : errorList) {
                errorsText.append(error.getText()).append("; ");
            }

            System.out.println("Validation errors captured: " + errorsText);

            // Assert expected validation error presence (Adjust assertion based on your scenario)
            Assert.assertTrue(errorsText.toString().contains("Phone number invalid"),
                    "Expected 'Phone number invalid' error message but got: " + errorsText);

        } else if (!confirmationCards.isEmpty() && confirmationCards.get(0).isDisplayed()) {
            // 2. Booking Success Branch
            WebElement confirmationCard = confirmationCards.get(0);

            Assert.assertEquals(confirmationCard.findElement(By.tagName("h2")).getText().trim(), "Book This Room");

            WebElement back = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/']")));
            js.executeScript("arguments[0].click();", back);

        } else {
            // 3. Fallback if neither container renders
            Assert.fail("Neither booking confirmation nor validation error alert was displayed.");
        }
    }

    @AfterTest
    public void quit(){
        if (driver != null){
            driver.quit();
        }
    }
}