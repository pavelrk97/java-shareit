package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final AtomicLong counter = new AtomicLong(1);
    private final Map<Long, Item> items = new HashMap<Long, Item>();

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        log.info("Created new item: {}", item.getId(), item.getName());
        return item;
    }

    @Override
    public Item update(Long itemId, Item updateItem, Long userId) {
        Item oldItem = items.get(itemId);

        Optional.ofNullable(oldItem).orElseThrow(() ->
                        new NotFoundException("Item not found id = " + itemId));

        if (!Objects.equals(oldItem.getOwner(), userId)) {
            throw new NotFoundException("You are not owner of this item");
        }

        Optional.ofNullable(updateItem.getName()).filter(s -> !s.isBlank())
                .ifPresent(oldItem::setName);  // проверка на ввод пробела и налл
        Optional.ofNullable(updateItem.getDescription()).ifPresent(oldItem::setDescription);
        Optional.ofNullable(updateItem.getAvailable()).ifPresent(oldItem::setAvailable);

        items.put(itemId, oldItem);
        log.info("Updated new item: id={}, name={}, description={}, available={}",
                itemId, oldItem.getName(), oldItem.getDescription(), oldItem.getAvailable());
        return oldItem;
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException("Item not found id = " + itemId));
        log.info("Received item: id={}, name={}, description={}, available={}",
                item.getId(), item.getName(), item.getDescription(), item.getAvailable());
        return item;
    }

    @Override
    public Collection<Item> findAll() {
        log.info("Список всех пользователей");
        return items.values();
    }

    @Override
    public List<Item> searchItems(String text) {
        String searchText = text.toLowerCase();
        List<Item> searchResults = items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        log.info("Поиск предметов по тексту: {}", text);
        return searchResults;
    }

    @Override
    public void deleteItem(Long itemId) {
        log.info("Удален предмет с ID: {}", itemId);
        items.remove(itemId);
    }

    private long getNextId() {
        long maxId = items.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
        return counter.updateAndGet(current -> Math.max(current, maxId + 1));
    }
}