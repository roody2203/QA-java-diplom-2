package ru.yandex.praktikum.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String[] ingredients;
}
