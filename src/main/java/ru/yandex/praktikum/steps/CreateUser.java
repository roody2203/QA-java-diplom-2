package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateUserRequest;

import static io.restassured.RestAssured.given;

public class CreateUser extends BaseApi {
    public final static String PATH = "/api/auth/register";

    @Step("Send POST request to /api/auth/register")
    public ValidatableResponse createUser(CreateUserRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .post(PATH)
                .then();
    }
}
