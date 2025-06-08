package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.GetOrdersRequest;

import static io.restassured.RestAssured.given;

public class GetOrders extends BaseApi {
    public final static String PATH = "/api/orders";
    public final static String PATH_ALL = "/api/orders/all";

    @Step("Send GET request to /api/orders")
    public ValidatableResponse getUserOrders(String userAccessToken, GetOrdersRequest request) {

        return given()
                .spec(requestSpecification)
                .header("Authorization", userAccessToken)
                .body(request)
                .when()
                .get(PATH)
                .then();
    }

    @Step("Send GET request to /api/orders/all")
    public ValidatableResponse getAllUsersOrders(GetOrdersRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .get(PATH_ALL)
                .then();
    }
}
