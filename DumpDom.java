import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import java.time.Duration;

public class DumpDom {
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
    Thread.sleep(10000);
    System.out.println(driver.getCurrentUrl());
    System.out.println("TITLE=" + driver.getTitle());
    String html = driver.getPageSource();
    System.out.println(html.substring(0, Math.min(3000, html.length())));
    driver.quit();
  }
}