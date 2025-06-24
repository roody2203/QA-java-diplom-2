package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.praktikum.dto.UpdateUserRequest;
import ru.yandex.praktikum.steps.UpdateUser;

public class UpdateUserWithoutAuthorizationTest extends BaseUserApiTest {
    private UpdateUser updateUser;

    private String email;//создаем поле email
    private String password;//создаем поле пароль
    private String name;//создаем поле name

    @Test
    @DisplayName("Check can't update email without authorization")
    public void checkCantUpdateEmailWithoutAuthorization() {
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();
        // Создаем объект класса updateUser
        updateUser = new UpdateUser();

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setEmail(email);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUserWithoutAuthorization(updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_UNAUTHORIZED);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", false);
        compareBodyMessage(updateResponse, "message", "You should be authorised");
    }

    @Test
    @DisplayName("Check can't update password without authorization")
    public void checkCantUpdatePasswordWithoutAuthorization() {
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();
        // Создаем объект класса updateUser
        updateUser = new UpdateUser();

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setPassword(password);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUserWithoutAuthorization(updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_UNAUTHORIZED);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", false);
        compareBodyMessage(updateResponse, "message", "You should be authorised");
    }

    @Test
    @DisplayName("Check can't update name without authorization")
    public void checkCantUpdateNameWithoutAuthorization() {
        email = generateRandomEmail();
        password = generateRandomPassword();
        name = generateRandomName();
        // Создаем объект класса updateUser
        updateUser = new UpdateUser();

        // Создаем объект класса UpdateUserRequest
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        // Передаем новые данные для изменений
        updateRequest.setName(name);
        // Изменяем данные
        ValidatableResponse updateResponse = updateUser.updateUserWithoutAuthorization(updateRequest);

        // Проверяем статус код
        compareStatusCode(updateResponse, HttpStatus.SC_UNAUTHORIZED);

        // Проверяем тело ответа
        compareBody(updateResponse, "success", false);
        compareBodyMessage(updateResponse, "message", "You should be authorised");
    }
}