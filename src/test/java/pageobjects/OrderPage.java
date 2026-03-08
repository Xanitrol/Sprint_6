package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.cssSelector(".select-search__input");
    private final By metroOptionSokolniki = By.xpath("//button[contains(.,'Сокольники')]");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[contains(@class,'Button_Button__ra12g') and normalize-space(.)='Далее']");

    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By day29 = By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month')) and normalize-space(.)='29']");
    private final By rentDropdown = By.xpath("//div[contains(@class,'Dropdown-placeholder') and normalize-space(.)='* Срок аренды']");
    private final By rentSixDays = By.xpath("//div[contains(@class,'Dropdown-option') and normalize-space(.)='шестеро суток']");
    private final By blackPearlCheckbox = By.id("black");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[normalize-space(.)='Заказать']");
    private final By confirmYesButton = By.xpath("//div[contains(@class,'Order_Modal')]//button[normalize-space(.)='Да']");
    private final By orderCreatedHeader = By.cssSelector("div[class^='Order_ModalHeader']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillFirstForm(String name, String surname, String address, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).sendKeys(name);
        wait.until(ExpectedConditions.visibilityOfElementLocated(surnameInput)).sendKeys(surname);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).sendKeys(address);

        wait.until(ExpectedConditions.elementToBeClickable(metroInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(metroOptionSokolniki)).click();

        typePhoneWithFallback(phoneInput, phone);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void fillSecondForm(String comment) {
        wait.until(ExpectedConditions.elementToBeClickable(dateInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(day29)).click();
        wait.until(ExpectedConditions.elementToBeClickable(rentDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(rentSixDays)).click();

        WebElement color = wait.until(ExpectedConditions.elementToBeClickable(blackPearlCheckbox));
        if (!color.isSelected()) {
            color.click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).sendKeys(comment);
    }

    public void clickOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmYesButton)).click();
    }

    public String getOrderCreatedText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderCreatedHeader)).getText();
    }

    private void typePhoneWithFallback(By locator, String phone) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.click();
        el.clear();
        el.sendKeys(phone);

        String value = el.getAttribute("value");
        if (value == null || value.trim().isEmpty()) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));",
                    el, phone
            );
        }
    }
}