package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.Marker;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    Long id;
    @NotBlank(message = "Название товара не может быть пустым или содержать только пробелы", groups = {Marker.OnCreate.class})
    String name;
    @NotBlank(message = "Описание товара не может быть пустым или содержать только пробелы", groups = {Marker.OnCreate.class})
    String description;
    @NotNull(message = "Статус о том, доступна или нет вещь для аренды обязателен", groups = {Marker.OnCreate.class})
    Boolean available;
    UserDto owner;
    BookingDto lastBooking;
    List<CommentDto> comments;
    BookingDto nextBooking;
}
