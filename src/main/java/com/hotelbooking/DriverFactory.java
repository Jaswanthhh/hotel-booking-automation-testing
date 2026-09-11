package com.hotelbooking;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Locale;

public final class DriverFactory {
    private static boolean chromeSetupDone = false;
    private static boolean edgeSetupDone = false;

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);

        if ("edge".equals(browser)) {
            try {
                return createEdgeDriver();
            } catch (Exception e) {
                System.err.println("EdgeDriver could not be initialized (" + e.getMessage() + "). Falling back to ChromeDriver.");
                return createChromeDriver();
            }
        }
        return createChromeDriver();
    }

    private static synchronized void setupChromeDriverOnce() {
        if (!chromeSetupDone) {
            try {
                WebDriverManager.chromedriver().setup();
            } catch (Exception ignored) {
                // Fallback to Selenium Manager built-in driver management
            }
            chromeSetupDone = true;
        }
    }

    private static synchronized void setupEdgeDriverOnce() {
        if (!edgeSetupDone) {
            try {
                WebDriverManager.edgedriver().setup();
            } catch (Exception ignored) {
                // Fallback to Selenium Manager built-in driver management
            }
            edgeSetupDone = true;
        }
    }

    private static WebDriver createChromeDriver() {
        setupChromeDriverOnce();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1440,1200");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-background-networking");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        setupEdgeDriverOnce();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1440,1200");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return new EdgeDriver(options);
    }
}
