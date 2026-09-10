public class BrowserFactory {

    public static WebDriver getDriver(String browser) {

        switch(browser.toLowerCase()) {

            case "edge":
                return new EdgeDriver();

            case "firefox":
                return new FirefoxDriver();

            default:
                return new ChromeDriver();
        }
    }
}