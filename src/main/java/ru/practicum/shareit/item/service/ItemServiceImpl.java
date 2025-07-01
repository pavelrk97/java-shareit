package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        return null;
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long userId) {
        return null;
    }

    @Override
    public ItemDto getItemDtoById(Long itemId) {
        return null;
    }

    @Override
    public Collection<ItemDto> getAllItemDtoByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return List.of();
    }

    @Override
    public void deleteItem(Long itemId) {

    }
}