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

    public RestApiBuilder withBaseUrl(String url) {
        spec = given().baseUri(url);
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

    public RequestSpecification build() {
        return spec;
    }

    public static RestApiBuilder getBuilder() {
        return new RestApiBuilder();
    }

    public static RestApiBuilder forUrl(String url) {
        return new RestApiBuilder(url);
    }
}
