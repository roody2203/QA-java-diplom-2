package ru.yandex.praktikum.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;

    private String password;

    private String name;
}
