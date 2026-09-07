public class Locator {

    //1.1 Список названий товаров.
    //div[contains(@class, 'product-card')]//h4

    //1.2. Список цен товаров
    //div[contains(@class, 'product-card')]//div[contains(text(), '?')]

    //1.3. Название товара с ценой 25
    //div[contains(@class, 'product-card')][@data-price='25']//h4

    //1.4. Цена товара с названием «Стакан»
    //.product-card[data-name="Стакан"] div[style*="color:var(--primary)"]


    //1.5. Список карточек товаров в корзине
    //#cart-items .cart-item

    //1.6. Кнопка корзины
    //#open-cart-btn
}
