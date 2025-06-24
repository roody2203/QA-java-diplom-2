package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class CreateOrder extends BaseApi {
    public final static String PATH = "/api/orders";

    @Step("Send POST request to /api/orders with auth")
    public ValidatableResponse createOrder(String userAccessToken, CreateOrderRequest request) {

        return given()
                .spec(requestSpecification)
                .header("Authorization", userAccessToken)
                .body(request)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Send POST request to /api/orders without auth")
    public ValidatableResponse createOrder(CreateOrderRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .post(PATH)
                .then();
    }
}
