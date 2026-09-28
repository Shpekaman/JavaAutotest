package page;

import io.qameta.allure.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPageAssert {

    private final LoginPage page;

    public LoginPageAssert(LoginPage page) {
        this.page = page;
    }

    @Step("UI-проверка: все элементы формы авторизации видны")
    public LoginPageAssert allElementsVisible() {
        assertThat(page.isLoginVisible()).as("Поле логина видно").isTrue();
        assertThat(page.isPasswordVisible()).as("Поле пароля видно").isTrue();
        assertThat(page.isButtonVisible()).as("Кнопка 'Войти' видна").isTrue();
        return this;
    }

    @Step("UI-проверка: поле логина содержит '{expected}'")
    public LoginPageAssert loginValue(String expected) {
        assertThat(page.getLoginValue())
                .as("Логин должен быть '%s'", expected).isEqualTo(expected);
        return this;
    }

    @Step("UI-проверка: поле пароля содержит ожидаемое значение")
    public LoginPageAssert passwordValue(String expected) {
        assertThat(page.getPasswordValue())
                .as("Пароль должен быть '%s'", expected).isEqualTo(expected);
        return this;
    }
}
