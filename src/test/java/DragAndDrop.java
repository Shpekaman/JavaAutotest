import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DragAndDrop {
    SelenideElement firstCard= $x("//*[@id=\"card-50\"]");
    SelenideElement cartButton= $x("//*[@id=\"open-cart-btn\"]");
    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    private void loginAdmin() {

        $("#username").setValue("admin");
        $("#password").setValue("secret123");
        $("button[type='submit']").click();
        $("#n-name").shouldBe(visible);
    }
//Домашка тема 7
    @Test
    public void dragAndDrop() {
        sleep(1000);
        firstCard.dragAndDrop(DragAndDropOptions.to(cartButton));
        sleep(2000);
    }


    @Test
    public void addToCartThanDeleteAndVerify() {
        sleep(1000);
        firstCard.dragAndDrop(DragAndDropOptions.to(cartButton));
        sleep(2000);
        cartButton.click();
        $("#cart-item-50 > button").click();
        SelenideElement empty = $("#empty-cart");
        empty.shouldBe(visible);
        String emptyText = empty.getText();
        assertThat(emptyText)
                .as("Должно появиться уведомление об обработке заказа")
                .contains("Пусто");
    }

 /* Дальше можно не читать
    @Test
    void anotherDragAndDrop() {
        actions().moveToElement(firstCard)
                .clickAndHold()
                .pause(1000)
                .moveToElement(cartButton)
                .release()
                .pause(2000)
                .perform();
    }

    @Test
    void SlowDragAndDrop() {
        int firstCardX = firstCard.getCoordinates().inViewPort().getX() + firstCard.getSize().getWidth() / 2;
        int firstCardY = firstCard.getCoordinates().inViewPort().getY() + firstCard.getSize().getHeight() / 2;
        int cartButtonX = cartButton.getCoordinates().inViewPort().getX() + cartButton.getSize().getWidth() / 2;
        int cartButtonY = cartButton.getCoordinates().inViewPort().getY() + cartButton.getSize().getHeight() / 2;

        int diffX = cartButtonX - firstCardX;
        int diffY = cartButtonY - firstCardY;

        sleep(1000);
        Actions dndActions = actions().moveToElement(firstCard).clickAndHold();

        for (int i = 0; i < 50; i++) {
            dndActions = dndActions.moveByOffset(diffX/50, diffY/50);
        }
        dndActions.release().perform();
        sleep(2000);
    }

 */


}
