package tests;

import org.testng.annotations.Test;
import pages.AdminRoomPage;

public class AdminRoomTest {

    @Test
    public void addValidRoom() {

        // The driver will come from the common framework
        AdminRoomPage roomPage = new AdminRoomPage(driver);

        roomPage.enterRoomNumber("201");
        roomPage.selectRoomType("Single");
        roomPage.selectAccessible("true");
        roomPage.enterRoomPrice("180");

        roomPage.selectWiFi();
        roomPage.selectTV();

        roomPage.clickCreateRoom();
    }
}