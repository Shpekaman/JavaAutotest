import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;

public class RestApiBuilder {

    private static final String BASE_URL = "http://localhost:8080";
    private RequestSpecification spec;

    public RestApiBuilder() {
        this(BASE_URL);
    }

    public RestApiBuilder(String baseUrl) {
        spec = given().baseUri(baseUrl);
    }

    // Метод для указания URL – возвращает этот же билдер (не создаёт новый)
    public RestApiBuilder withBaseUrl(String url) {
        spec = given().baseUri(url); // пересоздаём спецификацию с новым URL
        return this;
    }

    public RestApiBuilder addAuth(String login, String password) {
        spec.auth().basic(login, password);
        return this;
    }

    public RestApiBuilder withContentType(String contentType) {
        spec.contentType(contentType);
        return this;
    }

    // Возвращает готовую спецификацию
    public RequestSpecification build() {
        return spec;
    }

    // Статические фабрики
    public static RestApiBuilder getBuilder() {
        return new RestApiBuilder();
    }

    public static RestApiBuilder forUrl(String url) {
        return new RestApiBuilder(url);
    }










}
