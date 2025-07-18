package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {
    String name;
    String description;
    Boolean available;
    Long owner;
}