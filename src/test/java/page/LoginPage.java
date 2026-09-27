package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LoginPage {

    private final SelenideElement loginInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("button[type='submit']");

    public LoginPage open(String baseUrl) {
        Selenide.open(baseUrl + "/login");
        return this;
    }

    public LoginPage login(String username, String password) {
        loginInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        loginButton.click();
        waitForRedirect();
        return this;
    }

    private void waitForRedirect() {
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline && url().contains("/login")) {
            sleep(200);
        }
    }

    public boolean isLoginVisible() {
        return loginInput.is(visible);
    }

    public boolean isPasswordVisible() {
        return passwordInput.is(visible);
    }

    public boolean isButtonVisible() {
        return loginButton.is(visible);
    }

}
