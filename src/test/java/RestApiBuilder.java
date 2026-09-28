import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class RestApiBuilder {

    private RequestSpecification spec;

    public RestApiBuilder() {
        Config config = Config.getInstance();
        spec = given().baseUri(config.getBaseUrl());
    }

    public RestApiBuilder(String baseUrl) {
        spec = given().baseUri(baseUrl);
    }

    @Step("API: указать базовый URL {url}")
    public RestApiBuilder withBaseUrl(String url) {
        spec = given().baseUri(url);
        return this;
    }

    @Step("API: добавить базовую аутентификацию (логин: {login})")
    public RestApiBuilder addAuth(String login, String password) {
        spec.auth().basic(login, password);
        return this;
    }

    @Step("API: задать Content-Type {contentType}")
    public RestApiBuilder withContentType(String contentType) {
        spec.contentType(contentType);
        return this;
    }

    @Step("API: собрать RequestSpecification")
    public RequestSpecification build() {
        return spec;
    }

    @Step("API: создать билдер запроса")
    public static RestApiBuilder getBuilder() {
        return new RestApiBuilder();
    }

    @Step("API: создать билдер запроса для URL {url}")
    public static RestApiBuilder forUrl(String url) {
        return new RestApiBuilder(url);
    }
}
