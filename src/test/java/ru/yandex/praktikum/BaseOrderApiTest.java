package ru.yandex.praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;

public class BaseOrderApiTest extends BaseUserApiTest {
    protected String userEmail;//создаем поле email
    protected String userPassword;//создаем поле пароль
    protected String userName;//создаем поле name

    protected String userAccessToken;

    @Override
    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        super.init();

        //создаем рандомные логин и пароль
        userEmail = generateRandomEmail();
        userPassword = generateRandomPassword();
        userName = generateRandomName();

        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(userEmail);
        request.setPassword(userPassword);
        request.setName(userName);
        createUser.createUser(request);

        createdUsers.put(request.getEmail(), request);

        // Создаем объект класса LoginUserRequest
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setEmail(userEmail);
        loginRequest.setPassword(userPassword);
        // Логинимся с данными из Before
        ValidatableResponse loginResponse = loginUser.loginUser(loginRequest);
        userAccessToken = loginResponse.extract().path("accessToken");
    }

    @Override
    @After
    public void teardown() {
        super.teardown();
    }
}
