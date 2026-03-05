package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FaqTestChrome {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final By cookieButton = By.xpath("//button[contains(.,'да все привыкли')]");

    @BeforeAll
    static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://qa-scooter.praktikum-services.ru/");
        clickCookieIfExists();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void checkFaqAnswer1() {
        checkAnswer(0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой.");
    }

    @Test
    void checkFaqAnswer2() {
        checkAnswer(1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.");
    }

    @Test
    void checkFaqAnswer3() {
        checkAnswer(2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.");
    }

    @Test
    void checkFaqAnswer4() {
        checkAnswer(3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее.");
    }

    @Test
    void checkFaqAnswer5() {
        checkAnswer(4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.");
    }

    @Test
    void checkFaqAnswer6() {
        checkAnswer(5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.");
    }

    @Test
    void checkFaqAnswer7() {
        checkAnswer(6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.");
    }

    @Test
    void checkFaqAnswer8() {
        checkAnswer(7, "Да, обязательно. Всем самокатов! И Москве, и Московской области.");
    }

    private static void checkAnswer(int index, String expectedText) {
        By question = By.id("accordion__heading-" + index);
        By answer = By.id("accordion__panel-" + index);

        WebElement q = wait.until(ExpectedConditions.elementToBeClickable(question));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", q);
        q.click();

        String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(answer)).getText();
        assertEquals(expectedText, actual);
    }

    private static void clickCookieIfExists() {
        try {
            if (!driver.findElements(cookieButton).isEmpty()) {
                driver.findElement(cookieButton).click();
            }
        } catch (Exception ignored) {
        }
    }
}