package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import java.util.List;


public class HomeTest extends BaseTest {
    private HomePage homePage;
    @BeforeMethod
    public void initPage() {
        homePage = new HomePage(driver);
        homePage.openWebsite();
    }
    @Test
    public void verifyPageLoads() {
        String title = homePage.getPageTitle();
        Assert.assertFalse(title.isBlank(), "Page title should not be empty");
        Assert.assertTrue(homePage.getCurrentUrl().contains("automationintesting.online"), "Incorrect website URL");
    }

    @Test
    public void verifyLogo() {
        Assert.assertTrue(homePage.isLogoDisplayed(), "Logo is not displayed");
    }

    @Test
    public void verifyNavigation() {
        Assert.assertTrue(homePage.isNavigationDisplayed(), "Navigation bar is not displayed");
        Assert.assertTrue(homePage.isRoomsLinkDisplayed(), "Rooms link is not displayed");
        Assert.assertTrue(homePage.isContactLinkDisplayed(), "Contact link is not displayed");
    }

    @Test
    public void verifyRoomsNavigation() {
        homePage.clickRooms();
        homePage.scrollToRooms();
        Assert.assertTrue(homePage.isRoomsSectionDisplayed(), "Rooms section is not displayed");
    }

    @Test
    public void verifyContactNavigation() {
        homePage.clickContact();
        homePage.scrollToContact();
        Assert.assertTrue(homePage.isContactSectionDisplayed(), "Contact section is not displayed");
    }

    @Test
    public void verifyRoomSection() {
        homePage.scrollToRooms();
        Assert.assertTrue(homePage.isRoomsSectionDisplayed(), "Room section is not displayed");
    }

    @Test
    public void verifyImportantImages() {
        List<WebElement> images = homePage.getImages();
        Assert.assertTrue(images.size() > 0, "No images found on homepage");

        for (WebElement image : images) {
            Assert.assertTrue(image.isDisplayed(), "An image is not displayed");
        }
    }

    @Test
    public void verifyFooter() {
        homePage.scrollToFooter();
        Assert.assertTrue(homePage.isFooterDisplayed(), "Footer is not displayed");
    }

    @Test
    public void verifyImportantLinks() {
        homePage.scrollToFooter();
        List<WebElement> links = homePage.getFooterLinks();
        Assert.assertTrue(links.size() > 0, "No footer links found");

        for (WebElement link : links) {
            Assert.assertTrue(link.isDisplayed(), "Footer link is not displayed");
        }
    }

    @Test
    public void verifyViewportBehavior() {
        driver.manage().window().setSize(new Dimension(1920, 1080));
        Assert.assertTrue(homePage.isNavigationDisplayed(), "Navigation is not displayed on desktop");

        driver.manage().window().setSize(new Dimension(768, 1024));
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed(), "Page is not displayed on tablet");

        driver.manage().window().setSize(new Dimension(375, 667));
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed(), "Page is not displayed on mobile");
    }
}