import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

//Глобальное подключение Allure-интеграций для всех тестов

public class AllureSetup implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        RestAssured.filters(new AllureRestAssured());
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().savePageSource(true).screenshots(true));
    }
}
