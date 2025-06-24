package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateOrderRequest;
import ru.yandex.praktikum.dto.GetOrdersRequest;
import ru.yandex.praktikum.steps.CreateOrder;
import ru.yandex.praktikum.steps.GetOrders;

public class GetUserOrderTest extends BaseOrderApiTest {
    private CreateOrder createOrder;

    private GetOrders getOrders;

    @Override
    @Before
    public void init() {
        super.init();

        createOrder = new CreateOrder();

        getOrders = new GetOrders();
    }

    @Override
    @After
    public void teardown()
    {
        super.teardown();
    }


    @Test
    @DisplayName("Check get orders with authorization")
    public void checkGetOrdersWithAuthorization() {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa76"};
        // Создаем запрос на создание заказа
        CreateOrderRequest request = new CreateOrderRequest();
        // Добавляем ингредиенты в заказ
        request.setIngredients(ingredients);
        // Создаем заказ с ингредиентами для зарегистрированного пользователя
        ValidatableResponse response = createOrder.createOrder(userAccessToken, request);

        // Получаем заказ пользователя
        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        getOrdersRequest.setAccessToken(userAccessToken);
        ValidatableResponse getOrderResponse = getOrders.getUserOrders(userAccessToken, getOrdersRequest);

        compareStatusCode(getOrderResponse, HttpStatus.SC_OK);
        compareBody(getOrderResponse, "success", true);
        compareBodyMessageIsNotNull(getOrderResponse, "orders");
        compareBodyMessageIsNotNull(getOrderResponse, "orders[0]._id");
        compareBodyMessageIsNotNull(getOrderResponse, "orders[0].status");
        compareBodyMessageIsNotNull(getOrderResponse, "orders[0].number");
        compareBodyMessageIsNotNull(getOrderResponse, "orders[0].createdAt");
        compareBodyMessageIsNotNull(getOrderResponse, "orders[0].updatedAt");
    }

    @Test
    @DisplayName("Check get orders without authorization")
    public void checkGetOrdersWithoutAuthorization() {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa76"};
        // Создаем запрос
        CreateOrderRequest request = new CreateOrderRequest();
        // Добавляем ингредиенты в запрос
        request.setIngredients(ingredients);
        // Создаем заказ с ингредиентами для незарегистрированного пользователя
        ValidatableResponse response = createOrder.createOrder(request);

        // Пытаемся получить заказ пользователя
        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        ValidatableResponse getOrderResponse = getOrders.getUserOrders("123456789", getOrdersRequest);

        compareStatusCode(getOrderResponse, HttpStatus.SC_UNAUTHORIZED);
        compareBody(getOrderResponse, "success", false);
        compareBodyMessage(getOrderResponse, "message",  "You should be authorised");
    }
}
