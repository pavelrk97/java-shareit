package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testItemDtoSerialization() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(new UserDto(1L, "Test User", "test@yandex.ru"))
                .lastBooking(new BookingDto())
                .comments(Collections.emptyList())
                .nextBooking(new BookingDto())
                .requestId(1L)
                .build();

        String json = objectMapper.writeValueAsString(itemDto);
        assertNotNull(json);
        ItemDto deserializedItemDto = objectMapper.readValue(json, ItemDto.class);
        assertEquals(itemDto, deserializedItemDto);
    }

    @Test
    void testItemDtoDeserialization() throws IOException {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"Test User\",\"email\":\"test@yandex.ru\"},\"lastBooking\":null,\"comments\":[],\"nextBooking\":null,\"requestId\":1}";
        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("Test Item", itemDto.getName());
        assertEquals("Test Description", itemDto.getDescription());
    }

    @Test
    void testItemDtoWithNullValues() throws IOException {
        String json = "{\"id\":null,\"name\":null,\"description\":null,\"available\":null,\"owner\":null,\"lastBooking\":null,\"comments\":null,\"nextBooking\":null,\"requestId\":null}";

        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        assertNull(itemDto.getId());
        assertNull(itemDto.getName());
        assertNull(itemDto.getDescription());
        assertNull(itemDto.getAvailable());
        assertNull(itemDto.getOwner());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getComments());
        assertNull(itemDto.getNextBooking());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void testItemDtoEmptyValues() throws IOException {
        ItemDto itemDto = ItemDto.builder().build();
        assertNull(itemDto.getId());
        assertNull(itemDto.getName());
        assertNull(itemDto.getDescription());
        assertNull(itemDto.getAvailable());
        assertNull(itemDto.getOwner());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getComments());
        assertNull(itemDto.getNextBooking());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void testItemDtoBlankName() {
        ItemDto itemDto = ItemDto.builder().name("").description("desc").available(true).build();
        assertEquals("", itemDto.getName());
    }

    @Test
    void testItemDtoBlankDescription() {
        ItemDto itemDto = ItemDto.builder().name("name").description("").available(true).build();
        assertEquals("", itemDto.getDescription());
    }

    @Test
    void testItemDtoWithNullFields() throws IOException {
        ItemDto itemDto = ItemDto.builder().build();
        String json = objectMapper.writeValueAsString(itemDto);
        ItemDto deserializedItemDto = objectMapper.readValue(json, ItemDto.class);
        assertEquals(itemDto, deserializedItemDto);
    }

    @Test
    void testItemDtoEmptyLists() throws IOException {
        ItemDto itemDto = ItemDto.builder().comments(Collections.emptyList()).build();
        String json = objectMapper.writeValueAsString(itemDto);
        ItemDto deserializedItemDto = objectMapper.readValue(json, ItemDto.class);
        assertEquals(itemDto.getComments(), deserializedItemDto.getComments());
    }
}