package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pageobjects.MainPage;
import pageobjects.OrderPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest extends BaseTest {

    static Stream<Arguments> orderData() {
        return Stream.of(
                Arguments.of("top", "Дмитрий", "Яковлев", "Москва", "89990000000", "Привет, друг!"),
                Arguments.of("middle", "Иван", "Петров", "Москва", "89991112233", "Позвоните заранее")
        );
    }

    @ParameterizedTest
    @MethodSource("orderData")
    public void createOrderFromTwoButtons(String buttonPlace,
                                          String name,
                                          String surname,
                                          String address,
                                          String phone,
                                          String comment) {

        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        mainPage.open();
        mainPage.acceptCookies();

        if ("top".equals(buttonPlace)) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickMiddleOrderButton();
        }

        orderPage.fillFirstForm(name, surname, address, phone);
        orderPage.clickNext();
        orderPage.fillSecondForm(comment);
        orderPage.clickOrder();
        orderPage.confirmOrder();

        assertTrue(orderPage.getOrderCreatedText().contains("Заказ оформлен"));
    }
}