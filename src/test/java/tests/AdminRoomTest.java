package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.AdminRoomPage;
import pages.LoginPage;

public class AdminRoomTest extends BaseTest {

    @Test
    public void addValidRoom() {

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin", "password");

        // Create room
        AdminRoomPage roomPage = new AdminRoomPage(driver);

        roomPage.enterRoomNumber("204");
        roomPage.selectRoomType("Single");
        roomPage.selectAccessible("true");
        roomPage.enterRoomPrice("180");
        roomPage.selectWiFi();
        roomPage.selectTV();
        roomPage.clickCreateRoom();

        // Verify room was created
        Assert.assertTrue(
                roomPage.isRoomDisplayed("204"),
                "Room 204 was not created"
        );
    }
}