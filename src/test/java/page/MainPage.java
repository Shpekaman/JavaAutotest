package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement cartButton = $("#open-cart-btn");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement toastContainer = $("#toast-container");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final SelenideElement productNameInput = $("#n-name");
    private final SelenideElement productPriceInput = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");

    @Step("UI: открыть главную страницу {baseUrl}")
    public MainPage open(String baseUrl) {
        Selenide.open(baseUrl);
        return this;
    }

    @Step("UI: указать количество {qty} для товара с id={id}")
    public MainPage setQuantity(Long id, String qty) {
        $("#q-" + id).shouldBe(visible).setValue(qty);
        return this;
    }

    @Step("UI: добавить товар с id={id} в корзину")
    public MainPage clickAddToCart(Long id) {
        $("button[data-action='add-to-cart'][data-id='" + id + "']").shouldBe(visible).click();
        return this;
    }

    @Step("UI: открыть корзину")
    public MainPage openCart() {
        cartButton.click();
        return this;
    }

    @Step("UI: нажать 'Оформить заказ'")
    public MainPage clickMakeOrder() {
        makeOrderButton.shouldBe(visible).click();
        return this;
    }

    @Step("UI: добавить товар '{name}' с ценой {price} через админку")
    public MainPage addProduct(String name, String price) {
        productNameInput.shouldBe(visible).setValue(name);
        productPriceInput.shouldBe(visible).setValue(price);
        addButton.click();
        return this;
    }

    @Step("UI: отредактировать товар id={id}: имя '{name}', цена {price}")
    public MainPage editProduct(Long id, String name, String price) {
        SelenideElement nameInput = $("#nm-" + id);
        nameInput.clear();
        nameInput.setValue(name);
        SelenideElement priceInput = $("#pr-" + id);
        priceInput.clear();
        priceInput.setValue(price);
        $("button[data-action='update'][data-id='" + id + "']").click();
        return this;
    }

    @Step("UI: получить счётчик корзины")
    public String getCartCount() {
        return cartCount.getText();
    }

    @Step("UI: получить общую сумму в корзине")
    public String getTotalPrice() {
        return totalPrice.getText();
    }

    @Step("UI-проверка: получить общую сумму в корзине числом")
    public double getTotalPriceAsDouble() {
        return Double.parseDouble(getTotalPrice().trim());
    }

    @Step("UI-проверка: количество позиций в корзине")
    public int getCartItemCount() {
        return $$("#cart-items .cart-item").size();
    }

    @Step("UI-проверка: виден ли товар '{name}' на витрине")
    public boolean isProductVisible(String name) {
        return $$(".product-card h4").findBy(text(name)).is(visible);
    }

    @Step("UI-проверка: есть ли товар '{name}' в корзине")
    public boolean isCartItemVisible(String name) {
        return $$("#cart-items .cart-item").findBy(text(name)).is(visible);
    }

    @Step("UI: дождаться появления уведомления с текстом '{expectedText}'")
    public void waitForToast(String expectedText) {
        toastContainer.shouldBe(text(expectedText));
    }

    @Step("UI: дождаться появления любого уведомления")
    public void waitForAnyToast() {
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (!toastContainer.getText().isEmpty()) return;
            } catch (Exception ignored) {}
            sleep(200);
        }
    }

    @Step("UI: получить текст уведомлений")
    public String getToastText() {
        return toastContainer.getText();
    }

    @Step("UI-проверка: видно ли уведомление")
    public boolean isToastVisible() {
        return toastContainer.is(visible) && !toastContainer.getText().isEmpty();
    }
}
