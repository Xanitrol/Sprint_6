package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderPage {

    private WebDriver driver;

    // Поля
    private By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Станция в выпадашке
    private By sokolnikiOption = By.xpath("//div[contains(@class,'select-search__select')]//div[text()='Сокольники']");

    // Кнопка "Далее"
    private By nextButton = By.xpath("//button[text()='Далее']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setName(String name) {
        driver.findElement(nameInput).sendKeys(name);
    }

    public void setSurname(String surname) {
        driver.findElement(surnameInput).sendKeys(surname);
    }

    public void setAddress(String address) {
        driver.findElement(addressInput).sendKeys(address);
    }

    public void selectMetroSokolniki() {
        driver.findElement(metroInput).click();
        driver.findElement(sokolnikiOption).click();
    }

    public void setPhone(String phone) {
        driver.findElement(phoneInput).sendKeys(phone);
    }

    public void clickNext() {
        driver.findElement(nextButton).click();
    }
}