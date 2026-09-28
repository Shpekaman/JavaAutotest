package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    private final SelenideElement loginInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("button[type='submit']");

    @Step("UI: открыть страницу авторизации {baseUrl}/login")
    public LoginPage open(String baseUrl) {
        Selenide.open(baseUrl + "/login");
        return this;
    }

    @Step("UI: авторизоваться под пользователем '{username}'")
    public LoginPage login(String username, String password) {
        loginInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        loginButton.click();
        waitForRedirect();
        return this;
    }

    @Step("UI: дождаться редиректа после входа")
    private void waitForRedirect() {
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline
                && WebDriverRunner.getWebDriver().getCurrentUrl().contains("/login")) {
            sleep(200);
        }
    }

    @Step("UI-проверка: поле логина видно")
    public boolean isLoginVisible() {
        return loginInput.is(visible);
    }

    @Step("UI-проверка: поле пароля видно")
    public boolean isPasswordVisible() {
        return passwordInput.is(visible);
    }

    @Step("UI-проверка: кнопка 'Войти' видна")
    public boolean isButtonVisible() {
        return loginButton.is(visible);
    }

    @Step("UI-проверка: значение поля логина")
    public String getLoginValue() {
        return loginInput.getValue();
    }

    @Step("UI-проверка: значение поля пароля")
    public String getPasswordValue() {
        return passwordInput.getValue();
    }
}
