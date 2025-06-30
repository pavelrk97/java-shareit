package ru.practicum.shareit.item.model;

import org.springframework.web.context.request.RequestScope;
import ru.practicum.shareit.request.ItemRequest;
import user.Userl

import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO Sprint add-controllers.
 */
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest scope;
}
