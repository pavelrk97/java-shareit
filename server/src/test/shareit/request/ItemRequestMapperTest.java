package shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {

    @Test
    void toItemRequestFromCreateDto() {
        User user = User.builder().id(1L).build();
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.now())
                .build();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequestFromCreateDto(user, itemRequestCreateDto);

        assertEquals(itemRequestCreateDto.getId(), itemRequest.getId());
        assertEquals(itemRequestCreateDto.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestCreateDto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void toItemRequestDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void toItemRequestWithItemDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.now())
                .build();
        List<ItemDto> items = Collections.singletonList(ItemDto.builder().id(1L).name("Item1").build());

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestWithItemDto(itemRequest, items);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
        assertEquals(items, itemRequestDto.getItems());
    }
}