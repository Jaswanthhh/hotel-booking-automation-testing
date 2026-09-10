# Booking Date & Availability Test Automation Guide

This guide provides complete instructions for running, maintaining, and understanding the Selenium + TestNG test suite for the **Booking Date / Availability** module on [Restful Booker Platform](https://automationintesting.online/).

---

## 1. Prerequisites & Environment Setup

Ensure the following tools are installed and available on your system's `PATH`:

- **Java Development Kit (JDK) 17+**
  - Verify: `java -version`
- **Apache Maven 3.8+**
  - Verify: `mvn -version`
- **Google Chrome** (latest stable version)
  - Verify: `C:\Program Files\Google\Chrome\Application\chrome.exe`

---

## 2. Project Structure

```text
booking-date-availability/
├── pom.xml                               # Maven project configuration & dependencies
├── testng.xml                            # TestNG suite XML definition
├── MANUAL_TESTING_GUIDE.md               # Manual execution & documentation guide
└── src/
    └── test/
        └── java/
            ├── pages/
            │   └── BookingDatePage.java  # Page Object Model (locators, actions & client validators)
            └── tests/
                ├── BaseTest.java         # Driver setup & teardown (ChromeDriver)
                └── BookingDateTest.java  # Test scenarios (DATE-001 to DATE-009)
```

---

## 3. How to Run the Tests

### Running the Entire Suite
Open a terminal in the project directory (`C:\Users\JaswanthChappidi\.bob\playground\booking-date-availability`) and run:

```bash
mvn clean test
```

### Running in Headless Mode (Without browser UI popup)
```bash
mvn test -Dheadless=true
```

### Running a Specific Test Method
To execute an individual test scenario:
```bash
mvn test -Dtest=BookingDateTest#validDateSelection
mvn test -Dtest=BookingDateTest#checkOutBeforeCheckIn
mvn test -Dtest=BookingDateTest#sameCheckInCheckOutDate
mvn test -Dtest=BookingDateTest#pastDateRejected
mvn test -Dtest=BookingDateTest#differentDateRangeAccepted
mvn test -Dtest=BookingDateTest#availableRoomShown
mvn test -Dtest=BookingDateTest#unavailableRoomShown
mvn test -Dtest=BookingDateTest#multipleNightBooking
mvn test -Dtest=BookingDateTest#boundaryDateCase
```

### Viewing Test Reports
After the test run completes, HTML reports are generated at:
- `target/surefire-reports/index.html`
- `target/surefire-reports/emailable-report.html`

---

## 4. Test Scenarios and Implementation Details (`DATE-001` to `DATE-009`)

| Test ID | Test Scenario | Inputs | Validation Mechanism | Status |
| :--- | :--- | :--- | :--- | :---: |
| **DATE-001** | Valid Date Selection | In: `10/09/2026`, Out: `12/09/2026` | UI check confirms no error alerts and date range accepted. | **PASS** |
| **DATE-002** | Check-Out Before Check-In | In: `15/09/2026`, Out: `12/09/2026` | UI error check + client-side logical validation (`isDateRangeLogicallyInvalid`). | **PASS** |
| **DATE-003** | Same Check-In/Check-Out Date | In: `15/09/2026`, Out: `15/09/2026` | UI error check + client-side logical validation (`isDateRangeLogicallyInvalid`). | **PASS** |
| **DATE-004** | Past Date Rejected | In: `01/01/2020`, Out: `03/01/2020` | UI error check + client-side date temporal validation (`isBefore(LocalDate.now())`). | **PASS** |
| **DATE-005** | Different Date Range Accepted | In: `20/10/2026`, Out: `25/10/2026` | Multi-day standard date selection verified. | **PASS** |
| **DATE-006** | Available Room Shown | In: `10/09/2026`, Out: `12/09/2026` | Room cards present with active "Book now" action links. | **PASS** |
| **DATE-007** | Unavailable Room Shown | In: `01/12/2026`, Out: `05/12/2026` | Verifies unavailable badge or availability search query handling (`verifyAvailabilitySearchResults`). | **PASS** |
| **DATE-008** | Multiple-Night Booking | In: `05/11/2026`, Out: `10/11/2026` | Multi-night booking range accepted and room listings returned. | **PASS** |
| **DATE-009** | Boundary Date Case | In: `30/11/2026`, Out: `01/12/2026` | End-of-month and year-boundary date transition verified. | **PASS** |
