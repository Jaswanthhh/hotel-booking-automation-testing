import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import java.time.Duration;

public class DumpSelectors {
  public static void main(String[] args) throws Exception {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    WebDriver driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get("https://automationintesting.online/");
    Thread.sleep(8000);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    String nav = (String) js.executeScript("return document.querySelector('nav') ? document.querySelector('nav').outerHTML : 'NO_NAV';");
    System.out.println("NAV_HTML=" + nav.substring(0, Math.min(1500, nav.length())));
    System.out.println("IMG_COUNT=" + js.executeScript("return document.querySelectorAll('img').length;"));
    System.out.println("A_COUNT=" + js.executeScript("return document.querySelectorAll('a').length;"));
    System.out.println("ROOM_SECTION=" + js.executeScript("return document.body.innerText.includes('Rooms') ? 'YES' : 'NO';"));
    System.out.println("CONTACT_SECTION=" + js.executeScript("return document.body.innerText.includes('Contact') ? 'YES' : 'NO';"));
    System.out.println("BODY_TEXT=" + ((String) js.executeScript("return document.body.innerText; ")).substring(0, 3000));
    driver.quit();
  }
}