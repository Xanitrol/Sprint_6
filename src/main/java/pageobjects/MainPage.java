package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage {

    private WebDriver driver;

    // URL главной
    private By pageUrl = By.xpath("//*[contains(@class,'Home_Page') or contains(@class,'App_App')]");

    // Верхняя кнопка "Заказать"
    private By topOrderButton = By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']");

    // Средняя кнопка "Заказать" (с доп. классом UltraBig)
    private By middleOrderButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and contains(@class,'Button_UltraBig__UU3Lp') and text()='Заказать']");

    // Блок "Вопросы о важном"
    private By faqBlock = By.xpath("//div[contains(@class,'Home_FourPart')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void clickTopOrderButton() {
        driver.findElement(topOrderButton).click();
    }

    public void clickMiddleOrderButton() {
        // средняя кнопка ниже — прокрутим к ней
        WebElement button = driver.findElement(middleOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
    }

    public void scrollToFaq() {
        WebElement faq = driver.findElement(faqBlock);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faq);
    }

    // Кнопка вопроса по номеру (1..8)
    public By getFaqQuestionButton(int number) {
        return By.id("accordion__heading-" + (number - 1));
    }

    // Панель ответа по номеру (1..8)
    public By getFaqAnswerPanel(int number) {
        return By.id("accordion__panel-" + (number - 1));
    }
}