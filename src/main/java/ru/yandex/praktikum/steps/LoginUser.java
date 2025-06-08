package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.LoginUserRequest;

import static io.restassured.RestAssured.given;

public class LoginUser extends BaseApi{
    public final static String PATH = "/api/auth/login";

    @Step("Send POST request to /api/auth/login")
    public ValidatableResponse loginUser(LoginUserRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .post(PATH)
                .then();
    }
}
