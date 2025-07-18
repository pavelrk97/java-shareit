package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemCreateDto itemCreateDto);

    ItemDto update(ItemUpdateDto itemUpdateDto, Long userId);

    ItemDto getItemDtoById(Long itemId);

    Collection<ItemDto> getAllItemDtoByUserId(Long userId);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long itemId);
}
