package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement cartButton = $("#open-cart-btn");
    private final SelenideElement toastContainer = $("#toast-container");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final SelenideElement productNameInput = $("#n-name");
    private final SelenideElement productPriceInput = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");

    public MainPage open(String baseUrl) {
        Selenide.open(baseUrl);
        return this;
    }

    public MainPage setQuantity(Long id, String qty) {
        $("#q-" + id).shouldBe(visible).setValue(qty);
        return this;
    }

    public MainPage clickAddToCart(Long id) {
        $("button[data-action='add-to-cart'][data-id='" + id + "']").shouldBe(visible).click();
        return this;
    }

    public MainPage openCart() {
        cartButton.click();
        return this;
    }

    public MainPage clickMakeOrder() {
        makeOrderButton.shouldBe(visible).click();
        return this;
    }

    public MainPage addProduct(String name, String price) {
        productNameInput.shouldBe(visible).setValue(name);
        productPriceInput.shouldBe(visible).setValue(price);
        addButton.click();
        return this;
    }

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



    public String getTotalPrice() {
        return totalPrice.getText();
    }

    public double getTotalPriceAsDouble() {
        return Double.parseDouble(getTotalPrice().trim());
    }

    public int getCartItemCount() {
        return $$("#cart-items .cart-item").size();
    }

    public boolean isProductVisible(String name) {
        return $$(".product-card h4").findBy(text(name)).is(visible);
    }

    public boolean isCartItemVisible(String name) {
        return $$("#cart-items .cart-item").findBy(text(name)).is(visible);
    }

    public void waitForToast(String expectedText) {
        toastContainer.shouldBe(text(expectedText));
    }

    public void waitForAnyToast() {
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (!toastContainer.getText().isEmpty()) return;
            } catch (Exception ignored) {}
            sleep(200);
        }
    }

    public String getToastText() {
        return toastContainer.getText();
    }

    public boolean isToastVisible() {
        return toastContainer.is(visible) && !toastContainer.getText().isEmpty();
    }
}
