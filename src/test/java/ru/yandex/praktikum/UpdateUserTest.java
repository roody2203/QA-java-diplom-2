package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateUserRequest;
import ru.yandex.praktikum.dto.LoginUserRequest;
import ru.yandex.praktikum.dto.UpdateUserRequest;
import ru.yandex.praktikum.steps.UpdateUser;

public class UpdateUserTest extends BaseUserApiTest {
    private UpdateUser updateUser;

    private String email;//создаем поле email
    private String password;//создаем поле пароль
    private String name;//создаем поле name

    @Override
    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        super.init();

        // Создаем объект класса updateUser
        updateUser = new UpdateUser();

        //создаем рандомные логин и пароль
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
    @DisplayName("Check update email with authorization")
    public void checkUpdateUserEmailWithAuthorization() {
        // Создаем данные для редактирования
        String newEmail = "forkstreet52@mail.ru";

        // Создаем объект класса LoginUserRequest
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        // Логинимся с данными из Before
        ValidatableResponse response = loginUser.loginUser(request);

        String accessToken = response.extract().path("accessToken");

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setEmail(newEmail);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUser(accessToken, updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_OK);

        createdUsers.remove(email);

        email = newEmail;

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setPassword(password);
        createUserRequest.setName(name);
        createdUsers.put(request.getEmail(), createUserRequest);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", true);
        compareBodyMessageIsNotNull(updateResponse, "user");
        compareBodyMessage(updateResponse, "user.email", newEmail);
    }

    @Test
    @DisplayName("Check update password with authorization")
    public void checkUpdateUserPasswordWithAuthorization() {
        // Создаем данные для редактирования
        String newPassword = "Gda1nvm.";

        // Создаем объект класса LoginUserRequest
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        // Логинимся с данными из Before
        ValidatableResponse response = loginUser.loginUser(request);
        String accessToken = response.extract().path("accessToken");

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setEmail(email);
        updateRequest.setPassword(newPassword);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUser(accessToken, updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_OK);

        password = newPassword;

        createdUsers.get(email).setPassword(password);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", true);
        compareBodyMessageIsNotNull(updateResponse, "user");
    }

    @Test
    @DisplayName("Check update name with authorization")
    public void checkUpdateUserNameWithAuthorization() {
        // Создаем данные для редактирования
        String newName = "Castle";

        // Создаем объект класса LoginUserRequest
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        // Логинимся с данными из Before
        ValidatableResponse response = loginUser.loginUser(request);
        String accessToken = response.extract().path("accessToken");

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setEmail(email);
        updateRequest.setName(newName);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUser(accessToken, updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_OK);

        name = newName;

        // Проверяем тело ответа
        compareBody(updateResponse, "success", true);
        compareBodyMessageIsNotNull(updateResponse, "user");
        compareBodyMessage(updateResponse, "user.name", newName);
    }

    @Test
    @DisplayName("Check update email the same value with authorization")
    public void checkUpdateUserEmailTheSameValueWithAuthorization() {
        // Создаем данные для редактирования
        String otherEmail = "strekoza@yandex.ru";
        String otherPassword = "strekoza123";
        String otherName = "strekoza";

        CreateUserRequest otherCreateUserRequest = new CreateUserRequest();
        otherCreateUserRequest.setEmail(otherEmail);
        otherCreateUserRequest.setPassword(otherPassword);
        otherCreateUserRequest.setName(otherName);

        ValidatableResponse otherResponse = createUser.createUser(otherCreateUserRequest);

        createdUsers.put(otherCreateUserRequest.getEmail(), otherCreateUserRequest);

        // Создаем объект класса LoginUserRequest
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail(email);
        request.setPassword(password);
        // Логинимся с данными из Before
        ValidatableResponse response = loginUser.loginUser(request);

        String accessToken = response.extract().path("accessToken");

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setEmail(otherEmail);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUser(accessToken, updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_FORBIDDEN);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", false);
        compareBodyMessage(updateResponse, "message", "User with such email already exists");
    }
}
