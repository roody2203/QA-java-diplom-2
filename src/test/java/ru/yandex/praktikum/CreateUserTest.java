package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateUserRequest;

public class CreateUserTest extends BaseUserApiTest {
    private String email;//создаем поле email
    private String password;//создаем поле пароль
    private String name;//создаем поле name

    @Override
    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
       super.init();
    }

    @Override
    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    @DisplayName("Check create user")
    public void createUserTest() {
        // Создаем рандомные email, пароль и имя
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();

        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        ValidatableResponse response = createUser.createUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);
        // Запоминаем созданного пользователя
        createdUsers.put(request.getEmail(), request);
        // Проверяем тело ответа
        compareBody(response, "success", true);
        compareBodyMessageIsNotNull(response,"accessToken");
        compareBodyMessageIsNotNull(response,"refreshToken");
    }

    @Test
    @DisplayName("Check can't create two identical users")
    public void checkCantCreateTwoIdenticalUsersTest() {
        // Создаем рандомные email, пароль и имя
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();

        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        ValidatableResponse firstResponse = createUser.createUser(request);
        ValidatableResponse secondResponse = createUser.createUser(request);
        // Проверяем статус код
        compareStatusCode(secondResponse, HttpStatus.SC_FORBIDDEN);
        // Проверяем тело ответа
        compareBody(secondResponse, "success", false);
        compareBodyMessage(secondResponse, "message", "User already exists");
    }

    @Test
    @DisplayName("Check can't create user without email")
    public void checkCantCreateUserWithoutEmail() {
        // Создаем пустой email, рандомные пароль и имя
        email = "";
        password = generateRandomPassword();
        name = generateRandomName();
        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        ValidatableResponse response = createUser.createUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_FORBIDDEN);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Check can't create user without password")
    public void checkCantCreateUserWithoutPassword() {
        // Создаем рандомный email, пустой пароль и рандомное имя
        email = generateRandomEmail();
        password = "";
        name = generateRandomName();
        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        ValidatableResponse response = createUser.createUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_FORBIDDEN);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Check create user without name")
    public void checkCantCreateUserWithoutName() {
        // Создаем рандомные email, пароль и пустое имя
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = "";
        // Создаем пользователя
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        ValidatableResponse response = createUser.createUser(request);
        // Проверяем статус код
        compareStatusCode(response, HttpStatus.SC_FORBIDDEN);
        // Проверяем тело ответа
        compareBody(response, "success", false);
        compareBodyMessage(response, "message", "Email, password and name are required fields");
    }
}
