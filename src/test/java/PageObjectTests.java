import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.LoginPageAssert;
import page.MainPage;
import page.MainPageAssert;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PageObjectTests {

    private final Config config = Config.getInstance();
    private final List<Long> createdProductIds = new ArrayList<>();

    private MainPage mainPage;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        mainPage = new MainPage();
        loginPage = new LoginPage();
    }

    @AfterEach
    @Step("Бизнес-шаг: очистка — удалить созданные тестом товары")
    void tearDown() {
        try {
            Cookies cookies = getAdminCookies();
            for (Long id : createdProductIds) {
                try {
                    given().cookies(cookies).delete("/goods/" + id).then();
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        createdProductIds.clear();
    }

    @Step("Бизнес-шаг: вход в админку")
    private void loginAdmin() {
        loginPage.open(config.getBaseUrl());
        new LoginPageAssert(loginPage).allElementsVisible();
        loginPage.login(config.getAdminLogin(), config.getAdminPassword());
    }

    @Step("API: получить куки администратора через POST /login")
    private Cookies getAdminCookies() {
        return given()
                .formParam("username", config.getAdminLogin())
                .formParam("password", config.getAdminPassword())
                .post("/login")
                .then().extract().detailedCookies();
    }

    @Step("API: создать товар '{name}' с ценой {price} через POST /goods/add")
    private long addGoodsViaApi(String name, int price) {
        ApiTest.Goods body = new ApiTest.Goods(name, price);
        Response resp = given().spec(ApiTest.authSpec)
                .body(body)
                .when().post("/goods/add");
        if (resp.statusCode() != 200) {
            throw new RuntimeException("POST /goods/add вернул " + resp.statusCode()
                    + " для товара '" + name + "': " + resp.body().asString());
        }
        long id = resp.jsonPath().getLong("data.id");
        createdProductIds.add(id);
        return id;
    }

    @Step("Бизнес-метод: сгенерировать уникальное имя товара на базе '{base}'")
    private static String unique(String base) {
        return base + "_" + System.nanoTime();
    }

    // 1 Три товара в корзину, оплата, проверка уведомления
    @Test
    @Step("UI-тест: три товара в корзину, оплата и проверка уведомления")
    void addThreeItemsAndPay() {
        String name = unique("Тест_корзина_3шт");
        long id = addGoodsViaApi(name, 50);

        mainPage.open(config.getBaseUrl())
                .setQuantity(id, "3")
                .clickAddToCart(id)
                .openCart();

        new MainPageAssert(mainPage)
                .totalPrice(150.0)
                .totalPriceNotExceed(300.0)
                .cartItemCount(1)
                .cartItemVisible(name);

        mainPage.clickMakeOrder();

        new MainPageAssert(mainPage)
                .toastVisible()
                .toastContains("Заказ принят в обработку");
    }

    // 2 Добавить разных товаров, проверка общей цены
    @Test
    @Step("UI-тест: несколько разных товаров и проверка общей цены в корзине")
    void checkTotalPriceForMultipleItems() {
        String nameA = unique("Товар_А");
        String nameB = unique("Товар_Б");
        long a = addGoodsViaApi(nameA, 75);
        long b = addGoodsViaApi(nameB, 120);

        mainPage.open(config.getBaseUrl())
                .clickAddToCart(a)
                .clickAddToCart(b)
                .openCart();

        new MainPageAssert(mainPage)
                .totalPrice(195.0)
                .cartItemCount(2)
                .cartItemVisible(nameA)
                .cartItemVisible(nameB);
    }

    // 3 Вход и добавление товара, проверка уведомления
    @Test
    @Step("UI-тест: вход в админку, добавление товара, проверка уведомления")
    void addProductInAdmin() {
        loginAdmin();

        mainPage.addProduct(unique("Тестовый_товар"), "999");

        new MainPageAssert(mainPage)
                .toastVisible()
                .toastContains("Товар успешно добавлен");
    }

    // 4 Вход, редактирование товара, проверка
    @Test
    @Step("UI-тест: вход в админку, редактирование товара и проверка изменений")
    void editProductInAdmin() {
        long ts = System.nanoTime();
        String before = "Продукт_ДО_" + ts;
        String after = "Продукт_ПОСЛЕ_" + ts;
        long id = addGoodsViaApi(before, 100);

        loginAdmin();

        mainPage.editProduct(id, after, "250");

        new MainPageAssert(mainPage).toastContains("обновлен");

        mainPage.open(config.getBaseUrl());
        new MainPageAssert(mainPage).productVisible(after);
    }
}
