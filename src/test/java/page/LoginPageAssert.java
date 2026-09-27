package page;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPageAssert {

    private final LoginPage page;

    public LoginPageAssert(LoginPage page) {
        this.page = page;
    }

    public LoginPageAssert allElementsVisible() {
        assertThat(page.isLoginVisible()).as("Поле логина видно").isTrue();
        assertThat(page.isPasswordVisible()).as("Поле пароля видно").isTrue();
        assertThat(page.isButtonVisible()).as("Кнопка 'Войти' видна").isTrue();
        return this;
    }

}
