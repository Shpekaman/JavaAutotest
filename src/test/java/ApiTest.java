import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

@Tag("apiTest")
public class ApiTest {


    //Задача номер 1

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

    //Удалить товар
    protected static void deleteGood(int id) {
        given()
                .spec(authSpec)
                .when()
                .delete("/goods/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    public void getGoodsList() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    @Test
    public void getGoodsListUsingRequestSpecification() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
        given()
                .spec(spec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    @Test
    public void addGoods() {
        String name = "good_" + UUID.randomUUID();
        float price = random.nextFloat(100);


        Response createResp = given()
                .spec(authSpec)
                .body(new Goods(name, price))
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();

        int id = createResp.path("data.id");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.find { it.id == " + id + " }.name", equalTo(name))
                .body("goods.find { it.id == " + id + " }.price", equalTo(price));
        deleteGood(createResp.path("data.id"));

    }

    @Test
    public void addGoods2() {
        String name = "good_" + UUID.randomUUID().toString();
        float price = random.nextFloat(100);

        Response createResp = given()
                .spec(authSpec)
                .body(new Goods(name, price))
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();

        int id = createResp.path("data.id");
        assertThat(id).as("ID созданного товара не должен быть null").isNotNull();

        Response response = given()
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        List<Goods> goodsList = response.jsonPath().getList("goods", Goods.class);

        assertThat(goodsList)
                .as("Список товаров не должен быть пустым")
                .isNotEmpty()
                .anySatisfy(good -> {
                    assertThat(good.id)
                            .as("ID товара")
                            .isEqualTo(id);
                    assertThat(good.name)
                            .as("Имя товара")
                            .isEqualTo(name);
                    assertThat(good.price)
                            .as("Цена товара")
                            .isEqualTo(price);
                });

        deleteGood(createResp.path("data.id"));
    }

}
