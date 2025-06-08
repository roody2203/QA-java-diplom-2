package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;
import ru.yandex.praktikum.steps.CreateUser;
import ru.yandex.praktikum.steps.DeleteUser;
import ru.yandex.praktikum.steps.LoginUser;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.Matchers.*;

public class BaseUserApiTest {
    protected CreateUser createUser;
    protected LoginUser loginUser;
    protected DeleteUser deleteUser;

    protected Map<String, CreateUserRequest> createdUsers;

    @Step("Generate random email")
    public String generateRandomEmail() {
        return RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
    }

    @Step("Generate random password")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Generate random name")
    public String generateRandomName() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    @Step("Compare response status code with expected status code")
    public  void compareStatusCode(ValidatableResponse response, int statusCode){
        response.statusCode(statusCode);
    }

    @Step("Compare response body with expected boolean")
    public  void compareBody(ValidatableResponse response, String result, boolean expectedBody) {
        response.body(result, is(expectedBody));
    }

    @Step("Compare response body message with expected string")
    public  void compareBodyMessage(ValidatableResponse response, String result, String expectedBody) {
        response.body(result, is(expectedBody));
    }

    @Step("Compare response body message with expected string array")
    public  void compareBodyMessage(ValidatableResponse response, String result, String[] expectedBody) {
        response.body(result, contains(expectedBody));
    }

    @Step("Compare response body message is Not Null")
    public  void compareBodyMessageIsNotNull(ValidatableResponse response, String result) {
        response.body(result, notNullValue());
    }

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        // Создаем объект класса CreateUser
        createUser = new CreateUser();
        //Создаем объект класса LoginUser(нужен для удаления курьера в аннотации After)
        loginUser = new LoginUser();
        // Создаем объект класса DeleteUser для удаления в аннотации After
        deleteUser = new DeleteUser();

        createdUsers = new TreeMap<String, CreateUserRequest>();
    }

    @After
    public void teardown() {
        //удаляем созданных курьеров
        for(Map.Entry<String, CreateUserRequest> entry : createdUsers.entrySet()) {
            LoginUserRequest request = new LoginUserRequest();
            request.setEmail(entry.getValue().getEmail());
            request.setPassword(entry.getValue().getPassword());
            String accessToken = loginUser.loginUser(request).extract().path("accessToken");
            deleteUser.deleteUser(accessToken);
        }
    }
}
