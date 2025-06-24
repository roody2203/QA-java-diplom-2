package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.DeleteUserRequest;

import static io.restassured.RestAssured.given;

public class DeleteUser extends BaseApi {
        public final static String PATH = "/api/auth/user";

        @Step("Send DELETE request to /api/auth/user")
        public ValidatableResponse deleteUser(String accessToken) {

            return given()
                    .spec(requestSpecification)
                    .header("Authorization", accessToken)
                    .when()
                    .delete(PATH)
                    .then();
        }
}
