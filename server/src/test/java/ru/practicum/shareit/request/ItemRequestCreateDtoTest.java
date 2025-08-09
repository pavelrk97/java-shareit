package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestCreateDtoTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> json;

    @Test
    void testItemRequestCreateDto() throws IOException {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description("Test item")
                .created(LocalDateTime.now())
                .build();

        JsonContent<ItemRequestCreateDto> result = json.write(itemRequestCreateDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test item");
    }

    @Test
    void testDeserializeItemRequestCreateDto() throws IOException {
        String jsonContent = "{\"id\": 1, \"description\": \"Test item\", \"created\": \"2023-10-26T10:00:00\"}";

        ItemRequestCreateDto itemRequestCreateDto = json.parse(jsonContent).getObject();

        assertThat(itemRequestCreateDto.getId()).isEqualTo(1L);
        assertThat(itemRequestCreateDto.getDescription()).isEqualTo("Test item");
    }

    @Test
    void testItemRequestCreateDtoNullDescription() throws IOException {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description(null)
                .created(LocalDateTime.now())
                .build();

        JsonContent<ItemRequestCreateDto> result = json.write(itemRequestCreateDto);

        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(null);
    }

    @Test
    void testDeserializeItemRequestCreateDtoMissingFields() throws IOException {
        String jsonContent = "{\"id\": 1, \"description\": \"Test item\"}";
        ItemRequestCreateDto itemRequestCreateDto = json.parse(jsonContent).getObject();

        assertThat(itemRequestCreateDto.getId()).isEqualTo(1L);
        assertThat(itemRequestCreateDto.getDescription()).isEqualTo("Test item");
    }

    @Test
    void testItemRequestCreateDtoWithEmptyDescription() throws IOException {
        String jsonContent = "{\"id\": 1, \"description\": \"\", \"created\": \"2023-10-26T10:00:00\"}";
        ItemRequestCreateDto itemRequestCreateDto = json.parse(jsonContent).getObject();
        assertThat(itemRequestCreateDto.getDescription()).isEqualTo("");
    }

    @Test
    void testDeserializeItemRequestCreateDtoEmptyJson() throws IOException {
        String jsonContent = "{}";

        ItemRequestCreateDto itemRequestCreateDto = json.parse(jsonContent).getObject();

        assertThat(itemRequestCreateDto.getId()).isNull();
        assertThat(itemRequestCreateDto.getDescription()).isNull();
    }
}