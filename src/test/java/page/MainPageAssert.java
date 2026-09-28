package page;

import io.qameta.allure.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class MainPageAssert {

    private final MainPage page;

    public MainPageAssert(MainPage page) {
        this.page = page;
    }

    @Step("UI-проверка: товар '{name}' виден на витрине")
    public MainPageAssert productVisible(String name) {
        assertThat(page.isProductVisible(name))
                .as("Товар '%s' должен быть виден", name).isTrue();
        return this;
    }

    @Step("UI-проверка: товар '{name}' присутствует в корзине")
    public MainPageAssert cartItemVisible(String name) {
        assertThat(page.isCartItemVisible(name))
                .as("Товар '%s' должен быть в корзине", name).isTrue();
        return this;
    }

    @Step("UI-проверка: общая сумма в корзине равна {expected}")
    public MainPageAssert totalPrice(double expected) {
        assertThat(page.getTotalPriceAsDouble())
                .as("Сумма в корзине должна быть %s", expected).isEqualTo(expected);
        return this;
    }

    @Step("UI-проверка: общая сумма в корзине не превышает {max}")
    public MainPageAssert totalPriceNotExceed(double max) {
        assertThat(page.getTotalPriceAsDouble())
                .as("Сумма не должна превышать %s", max).isLessThanOrEqualTo(max);
        return this;
    }

    @Step("UI-проверка: позиций в корзине {expected}")
    public MainPageAssert cartItemCount(int expected) {
        assertThat(page.getCartItemCount())
                .as("Позиций в корзине должно быть %s", expected).isEqualTo(expected);
        return this;
    }

    @Step("UI-проверка: уведомление отображается")
    public MainPageAssert toastVisible() {
        page.waitForAnyToast();
        assertThat(page.isToastVisible())
                .as("Уведомление должно быть видно").isTrue();
        return this;
    }

    @Step("UI-проверка: уведомление содержит текст '{expectedText}'")
    public MainPageAssert toastContains(String expectedText) {
        page.waitForToast(expectedText);
        assertThat(page.getToastText())
                .as("Уведомление должно содержать '%s'", expectedText).contains(expectedText);
        return this;
    }
}
