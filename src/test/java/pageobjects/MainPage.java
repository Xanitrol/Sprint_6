package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cookieButton = By.xpath("//button[contains(.,'да все привыкли')]");
    private final By topOrderButton = By.xpath("(//button[contains(@class,'Button_Button__ra12g') and normalize-space(.)='Заказать'])[1]");
    private final By middleOrderButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and contains(@class,'Button_UltraBig__UU3Lp') and normalize-space(.)='Заказать']");
    private final By faqBlock = By.xpath("//div[contains(@class,'Home_FourPart')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    public void acceptCookies() {
        if (!driver.findElements(cookieButton).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
        }
    }

    public void clickTopOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
    }

    public void clickMiddleOrderButton() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(middleOrderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public void scrollToFaq() {
        WebElement faq = wait.until(ExpectedConditions.presenceOfElementLocated(faqBlock));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faq);
    }

    public void clickFaqQuestion(int index) {
        By question = By.id("accordion__heading-" + index);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(question));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public String getFaqAnswerText(int index) {
        By answer = By.id("accordion__panel-" + index);
        wait.until(driver -> !driver.findElement(answer).getText().isEmpty());
        return driver.findElement(answer).getText();
    }
}