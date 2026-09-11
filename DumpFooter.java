import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import java.time.Duration;

public class DumpFooter {
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
    String footer = (String) js.executeScript("return document.querySelector('footer') ? document.querySelector('footer').outerHTML : 'NO_FOOTER';");
    System.out.println("FOOTER=" + footer.substring(0, Math.min(2000, footer.length())));
    Object links = js.executeScript("return Array.from(document.querySelectorAll('a')).map(a => a.textContent.trim()).filter(Boolean).slice(0,20).join(' | ');");
    System.out.println("LINKS=" + links);
    Object sections = js.executeScript("return Array.from(document.querySelectorAll('section')).map(s => s.id || s.className || s.innerText.substring(0,30)).slice(0,20).join(' || ');");
    System.out.println("SECTIONS=" + sections);
    driver.quit();
  }
}