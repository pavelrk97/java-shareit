package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDto {

    @NotBlank(message = "Сообщение не может быть пустым или содержать только пробелы!")
    String text;
}