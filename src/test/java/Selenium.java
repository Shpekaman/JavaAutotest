import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//Первая задача
public class Selenium {
        private static final String BASE_URL = "http://localhost:8080";
        private static final String ADMIN_LOGIN = "admin";
        private static final String ADMIN_PASSWORD = "secret123";

        private WebDriver browser;
        private WebDriverWait wait;

        @BeforeEach
        public void setUp() {
            browser = new ChromeDriver();
            browser.manage().window().maximize();
            wait = new WebDriverWait(browser, Duration.ofSeconds(10));
        }

        @AfterEach
        public void tearDown() {
            if (browser != null) {
                browser.quit();
            }
        }

        private void loginAdmin() {
            browser.get(BASE_URL + "/login");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")))
                    .sendKeys(ADMIN_LOGIN);
            browser.findElement(By.id("password")).sendKeys(ADMIN_PASSWORD);
            browser.findElement(By.cssSelector("button[type='submit']")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("n-name")));
        }

    @Test
    public void addProductAndVerify() {
        String productName = "Тестовый_товар";
        String productPrice = "999";

        loginAdmin();

        browser.findElement(By.id("n-name")).sendKeys(productName);
        browser.findElement(By.id("n-price")).sendKeys(productPrice);
        browser.findElement(By.id("add-btn")).click();

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        browser.get(BASE_URL);

        List<WebElement> productNames = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".product-card h4"))
        );

        List<String> namesText = productNames.stream()
                .map(WebElement::getText)
                .toList();

        assertThat(namesText)
                .as("Товар '%s' должен отображаться на витрине", productName)
                .contains(productName);
    }

    @Test
    public void addProductToCartAndVerify() {
        String productName = "Стакан";

        loginAdmin();
        browser.get(BASE_URL);

        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".product-card button[data-name='" + productName + "']")
                )
        );
        addButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))).click();

        List<WebElement> cartItems = new WebDriverWait(browser, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector("#cart-items .cart-item")
                ));

        List<String> itemsText = cartItems.stream()
                .map(WebElement::getText)
                .toList();

        assertThat(itemsText)
                .as("Товар '%s' должен отображаться в корзине", productName)
                .anyMatch(text -> text.contains(productName));
    }

    @Test
    public void loginWithWrongLogPass() {
        browser.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")))
                .sendKeys("wrong_user");
        browser.findElement(By.id("password")).sendKeys("wrong_pass");
        browser.findElement(By.cssSelector("button[type='submit']")).click();

        // Ожидаем появления сообщения об ошибке с конкретным текстом
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(), 'Неверные учетные данные пользователя')]")
                )
        );
        assertThat(errorMessage.isDisplayed())
                .as("Должно отображаться сообщение об ошибке 'Неверные учетные данные пользователя'")
                .isTrue();
    }
//Считаю что корзина не должна быть пуста после рефреша
    @Test
    public void cartAfterRefresh() {
        String productName = "Стекло";

        browser.get(BASE_URL);

        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".product-card button[data-name='" + productName + "']")
                )
        );
        addButton.click();

        String countBefore = browser.findElement(By.id("cart-count")).getText();

        browser.navigate().refresh();

        String countAfter = browser.findElement(By.id("cart-count")).getText();

        assertThat(countAfter)
                .as("Корзина не должна быть пуста")
                .isNotEqualTo("0");

        assertThat(countAfter)
                .as("Количество товаров в корзине не должно измениться после обновления")
                .isEqualTo(countBefore);
    }
}

