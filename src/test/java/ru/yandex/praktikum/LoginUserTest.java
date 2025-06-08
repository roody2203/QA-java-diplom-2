package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;

public class LoginUserTest extends BaseUserApiTest {
    private String email;//создаем поле email
    private String password;//создаем поле пароль
    private String name;//создаем поле name

    @Override
    @Before
    public void init() {
        super.init();

        // Создаем рандомные логин и пароль
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();

        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        createUser.createUser(request);

        createdUsers.put(request.getEmail(), request);
    }

    @Override
    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    @DisplayName("Check login user")
    public void checkLoginUserTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);
        // Проверяем тело ответа
        compareBodyMessageIsNotNull(response, "accessToken");
        compareBodyMessageIsNotNull(response, "refreshToken");
    }

    @Test
    @DisplayName("Check login user without email")
    public void checkLoginUserWithoutEmailTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("");
        request.setPassword(password);
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user without password")
    public void checkLoginUserWithoutPasswordTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword("");
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user with fake email")
    public void checkLoginUserWithFakeEmailTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("Pisuke");
        request.setPassword(password);
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user with fake password")
    public void checkLoginUserWithFakePasswordTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword("password");
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user with fake email and password")
    public void checkLoginUserWithFakeEmailAndPasswordTest() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("Pisuke");
        request.setPassword("password");
        // Логинимся с данными созданного пользователя
        ValidatableResponse response = loginUser.loginUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "email or password are incorrect");
    }
}
