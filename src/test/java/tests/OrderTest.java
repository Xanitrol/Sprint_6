package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // ---------- ЛОКАТОРЫ: главная ----------
    private final By cookieButton = By.xpath("//button[contains(.,'да все привыкли')]");
    private final By topOrderButton = By.xpath("(//button[contains(@class,'Button_Button__ra12g') and normalize-space(.)='Заказать'])[1]");
    private final By middleOrderButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and contains(@class,'Button_UltraBig__UU3Lp') and normalize-space(.)='Заказать']");

    // ---------- ЛОКАТОРЫ: форма 1 ----------
    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.cssSelector(".select-search__input");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By metroOptionSokolniki = By.xpath("//div[contains(@class,'select-search__select')]//button[contains(.,'Сокольники')]");
    private final By nextButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and normalize-space(.)='Далее']");

    // ---------- ЛОКАТОРЫ: форма 2 ----------
    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By day29 = By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month')) and normalize-space(.)='29']");
    private final By rentDropdown = By.xpath("//div[contains(@class,'Dropdown-placeholder') and normalize-space(.)='* Срок аренды']");
    private final By rentSixDays = By.xpath("//div[contains(@class,'Dropdown-option') and normalize-space(.)='шестеро суток']");
    private final By blackPearlCheckbox = By.id("black");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[normalize-space(.)='Заказать']");

    // ---------- МОДАЛКИ ----------
    private final By confirmYesButton = By.xpath("//div[contains(@class,'Order_Modal')]//button[normalize-space(.)='Да']");
    // важный фикс: класс у заголовка динамический, поэтому ловим "starts-with"
    private final By orderCreatedHeader = By.cssSelector("div[class^='Order_ModalHeader']");
    private final By viewStatusButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and normalize-space(.)='Посмотреть статус']");

    @BeforeEach
    void setUp() {
        // driver создаём внутри теста (параметризация по браузеру)
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("chrome", "top"),
                Arguments.of("chrome", "middle"),
                Arguments.of("firefox", "top"),
                Arguments.of("firefox", "middle")
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void createOrderFromTwoButtons(String browser, String buttonPlace) {

        driver = createDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://qa-scooter.praktikum-services.ru/");

        // ждём, что главная реально прогрузилась
        wait.until(ExpectedConditions.visibilityOfElementLocated(topOrderButton));

        clickCookieIfExists();

        // 1) Нажать “Заказать” (сверху или в середине)
        if (buttonPlace.equals("top")) {
            wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
        } else {
            WebElement midBtn = wait.until(ExpectedConditions.presenceOfElementLocated(middleOrderButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", midBtn);
            clickByJs(middleOrderButton);
        }

        // 2) Форма "Для кого самокат"
        type(nameInput, "Дмитрий");
        type(surnameInput, "Яковлев");
        type(addressInput, "Москва");

        wait.until(ExpectedConditions.elementToBeClickable(metroInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(metroOptionSokolniki)).click();

        // фикс для Firefox: телефон иногда "не вводится" через sendKeys из-за маски
        typePhoneWithFallback(phoneInput, "89990000000");

        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();

        // 3) Форма "Про аренду"
        wait.until(ExpectedConditions.elementToBeClickable(dateInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(day29)).click();

        wait.until(ExpectedConditions.elementToBeClickable(rentDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(rentSixDays)).click();

        WebElement black = wait.until(ExpectedConditions.elementToBeClickable(blackPearlCheckbox));
        if (!black.isSelected()) {
            black.click();
        }

        type(commentInput, "Привет, друг!");

        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();

        // 4) Модалка “Хотите оформить заказ?” -> “Да”
        wait.until(ExpectedConditions.elementToBeClickable(confirmYesButton)).click();

        // 5) Проверка “Заказ оформлен”
        WebElement created = wait.until(ExpectedConditions.visibilityOfElementLocated(orderCreatedHeader));
        assertTrue(created.isDisplayed(), "Не появилось окно 'Заказ оформлен'");

        // и кликаем "Посмотреть статус" (в Firefox/Chrome иногда перекрыто — жмём через JS)
        clickByJs(viewStatusButton);
    }

    private WebDriver createDriver(String browser) {
        WebDriver drv;
        if (browser.equals("firefox")) {
            drv = new FirefoxDriver();
        } else {
            drv = new ChromeDriver();
        }

        // полный экран (и подстраховка размером)
        try {
            drv.manage().window().maximize();
        } catch (Exception ignored) { }
        try {
            drv.manage().window().setSize(new Dimension(1920, 1080));
        } catch (Exception ignored) { }

        return drv;
    }

    private void clickCookieIfExists() {
        try {
            if (!driver.findElements(cookieButton).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
            }
        } catch (Exception ignored) {
        }
    }

    private void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.click();
        el.clear();
        el.sendKeys(text);
    }

    private void typePhoneWithFallback(By locator, String phone) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.click();
        el.clear();
        el.sendKeys(phone);

        // если Firefox "не напечатал" — добиваем через JS + событие input
        String value = el.getAttribute("value");
        if (value == null || value.trim().isEmpty()) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));",
                    el, phone
            );
        }
    }

    private void clickByJs(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}