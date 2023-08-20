package ru.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private String status; // Код статуса HTTP-ответа
    private String reason; // Общее описание причины ошибки
    private String message; // Сообщение об ошибке
    private String timestamp; // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
