package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RoomsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By roomsLink = By.linkText("Rooms");
    private final By roomsHeading =
            By.xpath("//*[normalize-space()='Our Rooms']");

    // Finds Book Now buttons/links regardless of the exact HTML structure.
    private final By bookButtons = By.xpath(
            "//*[self::a or self::button]" +
                    "[contains(translate(normalize-space(.), " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                    "'abcdefghijklmnopqrstuvwxyz'), 'book')]"
    );

    public RoomsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickRooms() {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement roomsLink =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.partialLinkText("Rooms")
                        )
                );

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView(true);",
                        roomsLink
                );

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].click();",
                        roomsLink
                );
    }

    private List<WebElement> getBookButtons() {

        List<WebElement> result = new ArrayList<>();

        for (WebElement button : driver.findElements(bookButtons)) {

            if (button.isDisplayed()) {
                result.add(button);
            }
        }

        return result;
    }

    /*
     * Finds the actual room container associated with each Book Now button.
     */
    public List<WebElement> getRoomCards() {

        List<WebElement> cards = new ArrayList<>();

        for (WebElement button : getBookButtons()) {

            WebElement card = findRoomContainer(button);

            if (card != null && !containsSameElement(cards, card)) {
                cards.add(card);
            }
        }

        return cards;
    }

    private WebElement findRoomContainer(WebElement button) {

        String script = """
                const b = arguments[0];
                let e = b;

                while (e && e !== document.body) {

                    const text = (e.innerText || '').trim();

                    const hasHeading =
                        !!e.querySelector('h1,h2,h3,h4,h5,h6');

                    const hasImage =
                        !!e.querySelector('img') ||
                        [...e.querySelectorAll('*')].some(x => {

                            const bg =
                                getComputedStyle(x).backgroundImage || '';

                            return bg && bg !== 'none';
                        });

                    if (hasHeading && hasImage && text.length > 30) {
                        return e;
                    }

                    e = e.parentElement;
                }

                return b.parentElement;
                """;

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(script, button);

        return result instanceof WebElement
                ? (WebElement) result
                : null;
    }

    private boolean containsSameElement(
            List<WebElement> elements,
            WebElement candidate) {

        for (WebElement element : elements) {

            try {
                if (element.equals(candidate)) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }

    public int getRoomCount() {
        return getRoomCards().size();
    }

    public boolean areRoomsDisplayed() {
        return getRoomCount() > 0;
    }

    private WebElement getCard(int index) {

        List<WebElement> cards = getRoomCards();

        if (index < 0 || index >= cards.size()) {

            throw new IllegalArgumentException(
                    "Invalid room index: " + index +
                            ". Available rooms: " + cards.size()
            );
        }

        return cards.get(index);
    }

    // ROOM-002
    public String getRoomName(int index) {

        WebElement card = getCard(index);

        List<WebElement> headings =
                card.findElements(
                        By.cssSelector("h1,h2,h3,h4,h5,h6")
                );

        for (WebElement heading : headings) {

            String text = heading.getText().trim();

            if (!text.isEmpty()
                    && !text.equalsIgnoreCase("Our Rooms")) {

                return text;
            }
        }

        return "";
    }

    // ROOM-003
    public boolean hasRoomImage(int index) {

        WebElement card = getCard(index);

        // Normal image
        if (!card.findElements(
                By.cssSelector("img[src]")
        ).isEmpty()) {

            return true;
        }

        // CSS background image
        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript("""
                                const e = arguments[0];

                                return [...e.querySelectorAll('*')]
                                    .some(x => {

                                        const bg =
                                            getComputedStyle(x)
                                            .backgroundImage || '';

                                        return bg && bg !== 'none';
                                    });
                                """, card);

        return Boolean.TRUE.equals(result);
    }

    // ROOM-004
    public String getRoomDescription(int index) {

        WebElement card = getCard(index);

        for (WebElement p :
                card.findElements(By.cssSelector("p"))) {

            String text = p.getText().trim();

            if (!text.isEmpty()) {
                return text;
            }
        }

        // Fallback for div-based descriptions
        String text = card.getText().trim();

        String[] lines = text.split("\\R");

        for (String line : lines) {

            String value = line.trim();

            if (value.length() > 30
                    && !value.toLowerCase()
                    .contains("book now")) {

                return value;
            }
        }

        return text;
    }

    // ROOM-005
    public String getRoomPrice(int index) {

        WebElement card = getCard(index);

        String text = card.getText().trim();

        for (String line : text.split("\\R")) {

            String value = line.trim();

            if (value.matches(
                    ".*([$£€₹]|\\d+\\s*(per night|/night)).*"
            )) {

                return value;
            }
        }

        return text;
    }

    // ROOM-006
    public boolean hasAmenities(int index) {

        WebElement card = getCard(index);

        String text =
                (card.getText() + " "
                        + card.getAttribute("aria-label") + " "
                        + card.getAttribute("title"))
                        .toLowerCase();

        if (containsAmenity(text)) {
            return true;
        }

        // Check accessibility labels and titles
        for (WebElement element :
                card.findElements(
                        By.cssSelector("[title],[aria-label],svg,i")
                )) {

            String label =
                    (safe(element.getAttribute("title"))
                            + " "
                            + safe(element.getAttribute("aria-label")))
                            .toLowerCase();

            if (containsAmenity(label)) {
                return true;
            }
        }

        // Icon/list based amenities
        return !card.findElements(
                By.cssSelector(
                        "ul, li, .fa, .fas, .amenities, [class*='amenit']"
                )
        ).isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private boolean containsAmenity(String text) {

        return text.contains("wifi")
                || text.contains("wi-fi")
                || text.contains("tv")
                || text.contains("safe")
                || text.contains("amenit")
                || text.contains("shower")
                || text.contains("bathroom")
                || text.contains("air conditioning")
                || text.contains("accessible");
    }

    // ROOM-007
    public boolean hasRoomDetails(int index) {

        return !getRoomName(index).isBlank()
                && !getRoomDescription(index).isBlank()
                && hasRoomImage(index)
                && hasAmenities(index)
                && !getRoomPrice(index).isBlank()
                && isBookButtonDisplayed(index);
    }

    // ROOM-008
    public boolean isBookButtonDisplayed(int index) {

        List<WebElement> buttons = getBookButtons();

        return index >= 0
                && index < buttons.size()
                && buttons.get(index).isDisplayed();
    }

    // ROOM-009
    public void clickBook(int index) {

        WebElement bookButton = getBookButtons().get(index);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                bookButton
        );

        try {
            bookButton.click();
        } catch (Exception e) {

            js.executeScript(
                    "arguments[0].click();",
                    bookButton
            );
        }
    }}