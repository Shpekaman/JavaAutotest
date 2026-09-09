import java.util.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Alert;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

//Третья Задача
public class Selenide3 {

        private static final String BASE_URL = "http://localhost:8080";
        private static final String ADMIN_LOGIN = "admin";
        private static final String ADMIN_PASSWORD = "secret123";

        private final List<Long> createdProductIds = new ArrayList<>();

        @BeforeEach
        public void setUp() {
            Configuration.browser = "chrome";
            Configuration.browserSize = "1920x1080";
        }

        @AfterEach
        public void tearDown() {
            Cookies adminCookies = getAdminCookies();
            for (Long id : createdProductIds) {
                try {
                    given()
                            .cookies(adminCookies)
                            .delete("/goods/" + id)
                            .then();

                } catch (Exception ignored) {}
            }
            createdProductIds.clear();
        }

        private void loginAdmin() {
            open(BASE_URL + "/login");
            $("#username").setValue(ADMIN_LOGIN);
            $("#password").setValue(ADMIN_PASSWORD);
            $("button[type='submit']").click();
            $("#n-name").shouldBe(visible);
        }

        private Cookies getAdminCookies() {
            return given()
                    .formParam("username", ADMIN_LOGIN)
                    .formParam("password", ADMIN_PASSWORD)
                    .post("/login")
                    .then()
                    .extract()
                    .detailedCookies();
        }

//Обращение к API
    private long addGoods(String name, int price) {

        Response createResp = given()
                .spec(ApiTest.authSpec)
                .body(new ApiTest.Goods(name, price))
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();

        long id = createResp.jsonPath().getLong("data.id");
        createdProductIds.add(id);
        return id;

    }

        private void openCart() {
            $("#open-cart-btn").click();
            sleep(500);
        }



        @Test
        public void addThreeItemsAndPay() {
            long productId = addGoods("Тест_корзина_3шт", 50);

            open(BASE_URL + "/");

            $("#q-" + productId).setValue("3");
            $("button[data-action=\"add-to-cart\"][data-id=\"" + productId + "\"]").click();

            sleep(1000);

            openCart();
            $("#total-price").shouldHave(text("150"));
            $("#makeOrder").click();

            sleep(1000);

            SelenideElement toast = $("#toast-container > div:nth-child(2)");
            toast.shouldBe(visible);
            String toastText = toast.getText();
            assertThat(toastText)
                    .as("Должно появиться уведомление об обработке заказа")
                    .contains("Заказ принят в обработку!");

        }

        @Test
        public void addItemsCheckPrice() {
            long productA = addGoods("Товар_А_проверка_суммы", 75);
            long productB = addGoods("Товар_Б_проверка_суммы", 120);

            open(BASE_URL + "/");

            $("button[data-action=\"add-to-cart\"][data-id=\"" + productA + "\"]").click();
            sleep(300);
            $("button[data-action=\"add-to-cart\"][data-id=\"" + productB + "\"]").click();
            sleep(300);

            openCart();

            String totalText = $("#total-price").getText();
            double total = Double.parseDouble(totalText.trim());
            assertThat(total)
                    .as("Общая сумма в корзине должна быть %s, а получена %s", 195.0, total)
                    .isEqualTo(195.0);

        }

        @Test
        public void AddProductNotification() {
            loginAdmin();

            String productName = "Тестовый_товар";
            String productPrice = "999";

            loginAdmin();
            $("#n-name").setValue(productName);
            $("#n-price").setValue(productPrice);
            $("#add-btn").click();

            sleep(1000);

            SelenideElement toast = $("#toast-container .toast");
            toast.shouldBe(visible);
            String toastText = toast.getText();
            assertThat(toastText)
                    .as("Должно появиться уведомление об успешном добавлении товара")
                    .contains("Товар успешно добавлен");

            Response listResponse = given()
                    .get("/goods/list?page=0&size=100")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            List<Integer> ids = listResponse.jsonPath().getList("goods.id");
            List<String> names = listResponse.jsonPath().getList("goods.name");

            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).equals(productName)) {
                    createdProductIds.add(ids.get(i).longValue());
                    break;
                }
            }
        }

        @Test
        public void editProduct() {
            String originalName = "Продукт ДО";
            int originalPrice = 100;
            long productId = addGoods(originalName, originalPrice);

            loginAdmin();
            sleep(5000);//чтобы видно было что "продукт До" существует
            SelenideElement nameInput = $("#nm-" + productId);
            nameInput.shouldBe(visible);

            String newName = "Продукт После";
            double newPrice = 250.0;

            nameInput.clear();
            nameInput.setValue(newName);

            SelenideElement priceInput = $("#pr-" + productId);
            priceInput.clear();
            priceInput.setValue(String.valueOf(newPrice));

            $("button[data-action='update'][data-id='" + productId + "']").click();
            sleep(1000);

            SelenideElement toast = $("#toast-container .toast");
            toast.shouldBe(visible);
            String toastText = toast.getText();
            assertThat(toastText)
                    .as("Должно появиться уведомление об обновлении товара")
                    .contains("обновлен");

            open(BASE_URL + "/");

            SelenideElement productCard = $(".product-card h4");
            productCard.shouldBe(visible);

            boolean found = false;
            for (SelenideElement card : $$(".product-card h4")) {
                if (card.getText().equals(newName)) {
                    found = true;
                    break;
                }
            }
            assertThat(found)
                    .as("Товар с новым названием '%s' должен отображаться на витрине", newName)
                    .isTrue();
            createdProductIds.add(productId);
        }




    }


