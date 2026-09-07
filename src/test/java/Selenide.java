import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;
//Вторая задача
public class Selenide {
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    private void loginAdmin() {
        open(BASE_URL + "/login");
        $("#username").setValue("admin");
        $("#password").setValue("secret123");
        $("button[type='submit']").click();
        $("#n-name").shouldBe(visible);
    }

    @Test
    public void addProductAndVerify() {
        String productName = "Тестовый_товар";
        String productPrice = "999";

        loginAdmin();
        $("#n-name").setValue(productName);
        $("#n-price").setValue(productPrice);
        $("#add-btn").click();

        sleep(1000);

        open(BASE_URL + "/");
        $$(".product-card h4").findBy(text(productName)).shouldBe(visible);
    }

    @Test
    public void addProductToCartAndVerify() {
        String productName = "Стакан";

        loginAdmin();
        open(BASE_URL + "/");

        $(".product-card button[data-name='" + productName + "']").click();
        $("#open-cart-btn").click();

        $("#cart-items .cart-item").shouldHave(text(productName));
    }

    @Test
    public void loginWithWrongLogPass() {
        open(BASE_URL + "/login");
        $("#username").setValue("wrong_user");
        $("#password").setValue("wrong_pass");
        $("button[type='submit']").click();

        $(".alert-danger").shouldHave(text("Неверные учетные данные пользователя"));
    }
    //Считаю что корзина не должна быть пуста после рефреша
    @Test
    public void cartAfterRefresh() {
        String productName = "Стекло";

        open(BASE_URL + "/");
        $(".product-card button[data-name='" + productName + "']").click();

        String countBefore = $("#cart-count").getText();

        executeJavaScript("location.reload()");

        $("#cart-count").shouldHave(text(countBefore));
    }

    @Test
    public void orderShowsAlert() {
        open(BASE_URL + "/");

        $(".product-card button[data-name='Стакан']").click();
        $(".product-card button[data-name='Стакан']").click();

        $("#open-cart-btn").click();
        $("#makeOrder").click();

        Alert activeAlert = switchTo().alert();
        String alertText = activeAlert.getText();
        activeAlert.accept();

        assertThat(alertText)
                .as("Текст алерта должен содержать сообщение о превышении лимита")
                .contains("превышает лимит 300 ₽");
    }
}
