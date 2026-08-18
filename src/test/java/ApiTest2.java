import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Tag("apiTest")
public class ApiTest2 {
    //Задача номер 2 (не стал тут дублировать get list они в первом задании покрыты)
    public static class Goods {
        private String name;
        private float price;
        private int id;

        public Goods() {
        }

        public Goods(String name, float price) {
            this.name = name;
            this.price = price;
        }

        // геттеры
        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }

        public int getId() {
            return id;
        }

    }



    //Для аутентификации
    static RequestSpecification authSpec = RestApiBuilder.getBuilder()
            .addAuth("admin", "secret123")
            .withContentType("application/json")
            .build();

    private static final Random random = new Random();

    //Сделать товар
    protected static Goods createGood(String name, float price) {
        Response response = given()
                .spec(authSpec)
                .body(new Goods(name, price))
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();
        int id = response.path("data.id");
        Goods good = new Goods(name, price);
        good.id = id;
        return good;
    }
    //Удалить товар
    protected static void deleteGood(int id) {
        given()
                .spec(authSpec)
                .when()
                .delete("/goods/{id}", id)
                .then()
                .statusCode(200);
    }

    //delete/goods/id
    @Test
    void deleteGood200and404() {

        Goods created = createGood("GetTest", 50.0f);


        Response deleteResponse = given()
                .spec(authSpec)
                .accept(ContentType.JSON)
                .when()
                .delete("/goods/{id}", created.id)
                .then()
                .extract().response();


        assertThat(deleteResponse.getStatusCode())
                .as("При удалении существующего товара должен вернуться код 200")
                .isEqualTo(200);


        Response getResponse = given()
                .spec(authSpec)
                .when()
                .get("/goods/{id}", created.id)
                .then()
                .extract().response();

        assertThat(getResponse.getStatusCode())
                .as("После удаления GET по ID должен возвращать 404")
                .isEqualTo(404);
    }

    @Test
    void deleteGood400() {
        Response response = given()
                .spec(authSpec)
                .when()
                .delete("/goods/{id}", 10.00)
                .then()
                .extract().response();

        assertThat(response.getStatusCode())
                .as("DELETE сломанный запрос должен вернуть 400")
                .isEqualTo(400);
    }
//какие-то корявые записи не удаляются из базы
    @Test
    void deleteGood500() {
        Response response = given()
                .spec(authSpec)
                .when()
                .delete("/goods/{id}", 50)
                .then()
                .extract().response();

        assertThat(response.getStatusCode())
                .as("Если в БД есть ID 50,51,52,53 то будет ошибка 500 иначе будет 200(нужный id надо подставить)")
                .isEqualTo(200);
    }



//get/goods/id
    //Должно быть 200, но тут баг поэтому 500
    @Test
    void getGood200() {
        Goods created = createGood("GetTest", 50.0f);
        Response response = given()
                .spec(authSpec)
                .when()
                .get("/goods/{id}", created.id)
                .then()
                .extract().response();
        int id = response.path("data.id");

        assertThat(response.getStatusCode())
                        .as("200")
                        .isEqualTo(200);
        assertThat(response.jsonPath().getInt("id")).isEqualTo(id);
        assertThat(response.jsonPath().getString("name")).isEqualTo(created.name);
        assertThat(response.jsonPath().getFloat("price")).isEqualTo(created.price);

        deleteGood(id);
    }

    //Опять странное поведение должно быть 404, но здесь 500
    @Test
    void getGood404() {
        Response response = given()
                .spec(authSpec)
                .when()
                .get("/goods/{id}", 999)
                .then()
                .extract().response();

        assertThat(response.getStatusCode())
                .as("несуществующий товар должен вернуть 404")
                .isEqualTo(404);
    }

    @Test
    void getGood400() {
        Response response = given()
                .spec(authSpec)
                .when()
                .get("/goods/{id}", "abc")
                .then()
                .extract().response();

        assertThat(response.getStatusCode())
                .as("неправильный формат должен вернуть 400")
                .isEqualTo(400);
    }

    //post/goods/add
    @Test
    void addGoods200() {

        Response response = given()
                .spec(authSpec)
                .body(new Goods("GetTest", 50.00F))
                .when()
                .post("/goods/add")
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("Должно быть 200")
                .isEqualTo(200);

        int id = response.path("data.id");
        deleteGood(id);

    }

    @Test
    void addGoods400() {

        Response response = given()
                .spec(authSpec)
                .body("{}")
                .when()
                .post("/goods/add")
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("Плохой запрос должно быть 400")
                .isEqualTo(400);

    }

    //patch/goods/id
    @Test
    void patchGoods200() {
        Goods created = createGood("PatchTest", 30.0f);
        String newName = "UpdatedName";
        float newPrice = 99.99f;

        Goods patchPayload = new Goods(newName, newPrice);
        Response response = given()
                .spec(authSpec)
                .body(patchPayload)
                .when()
                .patch("/goods/{id}", created.id)
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("Должно быть 200")
                .isEqualTo(200);

        assertThat(response.jsonPath().getInt("id")).isEqualTo(created.id);
        assertThat(response.jsonPath().getString("name")).isEqualTo(newName);
        assertThat(response.jsonPath().getFloat("price")).isEqualTo(newPrice);

        deleteGood(created.id);
    }

    @Test
    void patchGoodOnlyName200() {
        Goods created = createGood("PartialTest", 40.0f);
        String newName = "OnlyNameChanged";

        // обновляем только имя
        String json = "{\"name\": \"" + newName + "\"}";
        Response response = given()
                .spec(authSpec)
                .body(json)
                .when()
                .patch("/goods/{id}", created.id)
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("Должно быть 200")
                .isEqualTo(200);

        assertThat(response.jsonPath().getInt("id")).isEqualTo(created.id);
        assertThat(response.jsonPath().getString("name")).isEqualTo(newName);
        assertThat(response.jsonPath().getFloat("price")).isEqualTo(40.0f);

        deleteGood(created.id);
    }

    @Test
    void patchGood404() {
        String json = "{\"name\": \"NonExist\"}";
        Response response = given()
                .spec(authSpec)
                .body(json)
                .when()
                .patch("/goods/{id}", 999999)
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("Изменение несуществующей записи должно быть 404")
                .isEqualTo(404);
    }

    @Test
    void patchGood400() {
        Goods created = createGood("PatchInvalid", 10.0f);
        String json = "{\"price\": \"not-a-number\"}";
        Response response = given()
                .spec(authSpec)
                .body(json)
                .when()
                .patch("/goods/{id}", created.id)
                .then()
                .extract().response();
        assertThat(response.getStatusCode())
                .as("бэд реквест должно быть 400")
                .isEqualTo(400);

        deleteGood(created.id);
    }
}
