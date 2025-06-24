package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.UpdateUserRequest;

import static io.restassured.RestAssured.given;

public class UpdateUser extends BaseApi {
    public final static String PATH = "/api/auth/user";

    @Step("Send PATCH request to /api/auth/user")
    public ValidatableResponse updateUser(String accessToken, UpdateUserRequest request) {

        return given()
                .spec(requestSpecification)
                .header("Authorization", accessToken)
                .body(request)
                .when()
                .patch(PATH)
                .then();
    }

    @Step("Send PATCH request without authorization to /api/auth/user")
    public ValidatableResponse updateUserWithoutAuthorization(UpdateUserRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .patch(PATH)
                .then();
    }
}
