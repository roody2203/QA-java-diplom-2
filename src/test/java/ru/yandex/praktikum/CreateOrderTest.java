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

public class CreateOrderTest extends BaseOrderApiTest {
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
    @DisplayName("Check create order with authorization")
    public void createOrderTest()
    {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa71"};

        CreateOrderRequest request = new CreateOrderRequest();
        request.setIngredients(ingredients);
        ValidatableResponse response = createOrder.createOrder(userAccessToken, request);

        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);

        // Проверяем тело ответа
        compareBody(response, "success", true);
        compareBodyMessageIsNotNull(response, "name");
        compareBodyMessageIsNotNull(response, "order");
        compareBodyMessageIsNotNull(response, "order.number");
    }

    @Test
    @DisplayName("Check create order without authorization")
    public void createOrderWithoutAuthorizationTest()
    {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa71"};

        CreateOrderRequest request = new CreateOrderRequest();
        request.setIngredients(ingredients);
        ValidatableResponse response = createOrder.createOrder(request);

        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);

        // Проверяем тело ответа
        compareBody(response, "success", true);
        compareBodyMessageIsNotNull(response, "name");
        compareBodyMessageIsNotNull(response, "order");
        compareBodyMessageIsNotNull(response, "order.number");
    }

    @Test
    @DisplayName("Check create order with ingredients")
    public void createOrderWithIngredientsTest()
    {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72"};

        CreateOrderRequest request = new CreateOrderRequest();
        request.setIngredients(ingredients);
        ValidatableResponse response = createOrder.createOrder(userAccessToken, request);

        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);

        // Проверяем тело ответа
        compareBody(response, "success", true);
        compareBodyMessageIsNotNull(response, "name");
        compareBodyMessageIsNotNull(response, "order");
        compareBodyMessageIsNotNull(response, "order.number");

        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        getOrdersRequest.setAccessToken(userAccessToken);
        ValidatableResponse getOrdersResponse = getOrders.getUserOrders(userAccessToken, getOrdersRequest);

        compareBodyMessage(getOrdersResponse, "orders[0].ingredients", ingredients);
    }

    @Test
    @DisplayName("Check create order without ingredients")
    public void createOrderWithoutIngredientsTest()
    {
        String[] ingredients = {};

        CreateOrderRequest request = new CreateOrderRequest();
        request.setIngredients(ingredients);
        ValidatableResponse response = createOrder.createOrder(userAccessToken, request);

        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_BAD_REQUEST);

        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Check create order with invalid ingredients")
    public void createOrderWithInvalidIngredientsTest()
    {
        String[] ingredients = {"61c0c5a71d", "61c0c5a71d1f"};

        CreateOrderRequest request = new CreateOrderRequest();
        request.setIngredients(ingredients);
        ValidatableResponse response = createOrder.createOrder(userAccessToken, request);

        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
