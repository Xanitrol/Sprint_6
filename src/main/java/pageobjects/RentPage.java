package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class RentPage {

    private WebDriver driver;

    // Дата
    private By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Срок аренды (плашка) + вариант
    private By rentPeriodDropdown = By.xpath("//div[@class='Dropdown-placeholder' and text()='* Срок аренды']");
    private By sixDaysOption = By.xpath("//div[@class='Dropdown-option' and text()='шестеро суток']");

    // Цвет: черный жемчуг
    private By blackPearlCheckbox = By.id("black");

    // Комментарий
    private By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка "Заказать" на странице аренды (не в хедере)
    private By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[text()='Заказать']");

    // Модалка подтверждения
    private By yesButtonInModal = By.xpath("//div[contains(@class,'Order_Modal')]//button[text()='Да']");

    // Заголовок успешного оформления
    private By successHeader = By.xpath("//div[contains(@class,'Order_ModalHeader') and contains(text(),'Заказ оформлен')]");

    public RentPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setDate(String date) {
        driver.findElement(dateInput).click();
        driver.findElement(dateInput).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(dateInput).sendKeys(date);
        driver.findElement(dateInput).sendKeys(Keys.ENTER);
    }

    public void selectSixDays() {
        driver.findElement(rentPeriodDropdown).click();
        driver.findElement(sixDaysOption).click();
    }

    public void chooseBlackPearl() {
        driver.findElement(blackPearlCheckbox).click();
    }

    public void setComment(String text) {
        driver.findElement(commentInput).sendKeys(text);
    }

    public void clickOrder() {
        driver.findElement(orderButton).click();
    }

    public void confirmYes() {
        driver.findElement(yesButtonInModal).click();
    }

    public By getSuccessHeader() {
        return successHeader;
    }
}