package tests;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.RoomsPage;

public class RoomsTest extends BaseTest{
    private RoomsPage rooms;

    @BeforeMethod
    public void initializePage() {
        rooms = new RoomsPage(driver);
    }

    // ROOM-001
    @Test
    public void verifyRoomsDisplayed() {

        rooms.clickRooms();

        Assert.assertTrue(
                rooms.areRoomsDisplayed(),
                "Rooms are not displayed"
        );
    }

    // ROOM-002
    @Test
    public void verifyRoomName() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        Assert.assertTrue(
                count > 0,
                "Expected room cards but found none"
        );

        for (int i = 0; i < count; i++) {

            Assert.assertFalse(
                    rooms.getRoomName(i).isBlank(),
                    "Room name is missing for room index " + i
            );
        }
    }

    // ROOM-003
    @Test
    public void verifyRoomImage() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertTrue(
                    rooms.hasRoomImage(i),
                    "Room image is missing for room index " + i
            );
        }
    }

    // ROOM-004
    @Test
    public void verifyRoomDescription() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertFalse(
                    rooms.getRoomDescription(i).isBlank(),
                    "Room description is missing for room index " + i
            );
        }
    }

    // ROOM-005
    @Test
    public void verifyRoomPrice() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertFalse(
                    rooms.getRoomPrice(i).isBlank(),
                    "Room price is missing for room index " + i
            );
        }
    }

    // ROOM-006
    @Test
    public void verifyAmenities() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertTrue(
                    rooms.hasAmenities(i),
                    "Amenities are missing for room index " + i
            );
        }
    }

    // ROOM-007
    @Test
    public void verifyRoomDetails() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertTrue(
                    rooms.hasRoomDetails(i),
                    "Complete room details are not available " +
                            "for room index " + i
            );
        }
    }

    // ROOM-008
    @Test
    public void verifyBookButton() {

        rooms.clickRooms();

        int count = rooms.getRoomCount();

        for (int i = 0; i < count; i++) {

            Assert.assertTrue(
                    rooms.isBookButtonDisplayed(i),
                    "Book button is not displayed for room index " + i
            );
        }
    }

    // ROOM-009
    @Test
    public void verifyNavigationToBooking() {

        rooms.clickRooms();

        String beforeUrl = driver.getCurrentUrl();

        rooms.clickBook(0);

        new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        ).until(d ->
                !d.getCurrentUrl().equals(beforeUrl)
                        || d.getPageSource()
                        .toLowerCase()
                        .contains("reserve")
                        || d.getPageSource()
                        .toLowerCase()
                        .contains("booking")
                        || d.getPageSource()
                        .toLowerCase()
                        .contains("firstname")
        );

        String pageSource =
                driver.getPageSource().toLowerCase();

        boolean bookingReached =
                !driver.getCurrentUrl().equals(beforeUrl)
                        || pageSource.contains("reserve")
                        || pageSource.contains("booking")
                        || pageSource.contains("firstname");

        Assert.assertTrue(
                bookingReached,
                "Book action did not navigate to or display " +
                        "the booking area."
        );
    }

    // ROOM-010
    @Test
    public void verifyExpectedRoomCardsLoad() {

        rooms.clickRooms();

        int roomCount = rooms.getRoomCount();

        Assert.assertTrue(
                roomCount > 0,
                "Expected at least one room card but found "
                        + roomCount
        );
    }
}