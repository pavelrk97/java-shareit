package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> itemRequestCreateDtoJacksonTester;

    @Autowired
    private JacksonTester<ItemRequestDto> itemRequestDtoJacksonTester;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testItemRequestCreateDtoSerialization() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime nowTruncated = now.truncatedTo(ChronoUnit.SECONDS);
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description("Test")
                .created(nowTruncated)
                .build();

        String jsonContent = itemRequestCreateDtoJacksonTester.write(itemRequestCreateDto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"description\":\"Test\"");
        assertThat(jsonContent).contains("\"created\":\"" + nowTruncated.format(FORMATTER) + "\"");
    }

    @Test
    void testItemRequestDtoDeserialization() throws IOException {
        String json = "{\"id\":1,\"description\":\"Test\",\"created\":\"2023-10-26T10:00:00\",\"items\":[]}";
        ItemRequestDto itemRequestDto = itemRequestDtoJacksonTester.parse(json).getObject();

        assertThat(itemRequestDto.getId()).isEqualTo(1L);
        assertThat(itemRequestDto.getDescription()).isEqualTo("Test");
        assertThat(itemRequestDto.getCreated()).isEqualTo(LocalDateTime.parse("2023-10-26T10:00:00", FORMATTER));
        assertThat(itemRequestDto.getItems()).isEmpty();
    }

    @Test
    void testItemRequestDtoSerializationWithItems() throws IOException {
        ItemDto itemDto = ItemDto.builder().id(1L).name("Item1").description("Desc1").available(true).requestId(1L).build();
        List<ItemDto> items = Collections.singletonList(itemDto);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test")
                .created(LocalDateTime.parse("2023-10-27T10:00:00", FORMATTER))
                .items(items)
                .build();

        String jsonContent = itemRequestDtoJacksonTester.write(itemRequestDto).getJson();

        String expectedJson = "{\"id\":1,\"description\":\"Test\",\"created\":\"2023-10-27T10:00:00\",\"items\":[{\"id\":1,\"name\":\"Item1\",\"description\":\"Desc1\",\"available\":true,\"owner\":null,\"lastBooking\":null,\"comments\":null,\"nextBooking\":null,\"requestId\":1}]}";
        assertThat(jsonContent).isEqualTo(expectedJson);
    }

    @Test
    void testItemRequestCreateDtoSerialization_emptyDescription() throws IOException {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description(null)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        String jsonContent = itemRequestCreateDtoJacksonTester.write(itemRequestCreateDto).getJson();

        assertThat(jsonContent).contains("\"description\":null");
    }

    @Test
    void testItemRequestCreateDtoDeserialization_emptyDescription() throws IOException {
        String json = "{\"id\":1,\"description\":null,\"created\":\"2023-10-27T10:00:00\"}";
        ItemRequestCreateDto itemRequestCreateDto = itemRequestCreateDtoJacksonTester.parse(json).getObject();

        assertThat(itemRequestCreateDto.getDescription()).isNull();
        assertThat(itemRequestCreateDto.getId()).isEqualTo(1L);
    }

    @Test
    void testItemRequestDtoSerialization_emptyItemList() throws IOException {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test")
                .created(LocalDateTime.parse("2023-10-27T10:00:00", FORMATTER))
                .items(Collections.emptyList())
                .build();

        String jsonContent = itemRequestDtoJacksonTester.write(itemRequestDto).getJson();

        assertThat(jsonContent).contains("\"items\":[]");
    }

    @Test
    void testItemRequestDtoDeserialization_missingFields() throws IOException {
        String json = "{\"description\":\"Test\"}";
        ItemRequestDto itemRequestDto = itemRequestDtoJacksonTester.parse(json).getObject();

        assertThat(itemRequestDto.getDescription()).isEqualTo("Test");
    }

    @Test
    void testItemRequestCreateDtoSerialization_blankDescription() throws IOException {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description(" ")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        String jsonContent = itemRequestCreateDtoJacksonTester.write(itemRequestCreateDto).getJson();
        assertThat(jsonContent).contains("\"description\":\" \"");
    }
}