package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.Marker;
import static ru.practicum.shareit.utils.HeaderConstants.USER_ID_HEADER;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long userId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemService.create(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId,
                          @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        itemUpdateDto.setId(itemId);
        return itemService.update(itemUpdateDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID_HEADER) Long userId,
                               @PathVariable("itemId") Long itemId) {
        return itemService.getItemDtoById(userId, itemId);
    }

    @GetMapping
    public Collection<ItemDto> getItemsByUserId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getAllItemDtoByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @Validated @RequestBody CommentCreateDto commentCreateDto,
                                    @PathVariable Long itemId) {

        return itemService.createComment(userId, commentCreateDto, itemId);
    }
}
