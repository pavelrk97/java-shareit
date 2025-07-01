package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {

        if (userRepository.findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        Item createdItem = itemRepository.create(item);
        log.info("Создан предмет с ID: {}", createdItem.getId());
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long userId) {
        Item itemFromDto = ItemMapper.toItem(itemDto);
        Item updatedItem = itemRepository.update(itemId, itemFromDto, userId);
        log.info("Обновлен предмет с ID: {}", itemId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemDtoById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Предмет с ID " + itemId + " не найден.");
        }
        log.info("Получен предмет с ID: {}", itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getAllItemDtoByUserId(Long userId) {
        return itemRepository.findAll().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.searchItems(text);
        log.info("Поиск предметов по тексту: {}", text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
        log.info("Удален предмет с ID: {}", itemId);
    }
}