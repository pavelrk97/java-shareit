package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();

        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);

        assertThat(userDtoSaved).hasJsonPath("$.id");
        assertThat(userDtoSaved).hasJsonPath("$.name");
        assertThat(userDtoSaved).hasJsonPath("$.email");

        assertThat(userDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(userDtoSaved).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    void testDeserialize() throws IOException {
        String json = "{\"id\":1,\"name\":\"Test\",\"email\":\"test@yandex.ru\"}";

        UserDto deserializedUserDto = jacksonTester.parseObject(json);

        assertThat(deserializedUserDto.getId()).isEqualTo(1L);
        assertThat(deserializedUserDto.getName()).isEqualTo("Test");
        assertThat(deserializedUserDto.getEmail()).isEqualTo("test@yandex.ru");
    }

    @Test
    void testSerializeWithNullName() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).email("test@yandex.ru").build();
        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);
        assertThat(userDtoSaved).hasJsonPath("$.name");
    }

    @Test
    void testDeserializeWithEmptyName() throws IOException {
        String json = "{\"id\":1,\"name\":\"\",\"email\":\"test@yandex.ru\"}";
        UserDto deserializedUserDto = jacksonTester.parseObject(json);
        assertThat(deserializedUserDto.getName()).isEqualTo("");
    }

    @Test
    void testSerializeWithEmptyEmail() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("").build();
        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);
        assertThat(userDtoSaved).hasJsonPath("$.email");
    }

    @Test
    void testDeserializeWithNullEmail() throws IOException {
        String json = "{\"id\":1,\"name\":\"Test\",\"email\":null}";
        UserDto deserializedUserDto = jacksonTester.parseObject(json);
        assertThat(deserializedUserDto.getEmail()).isNull();
    }

    @Test
    void testSerializeEmptyNameAndNullEmail() throws Exception {
        UserDto userDto = UserDto.builder().id(3L).name("").email(null).build();
        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);
        assertThat(userDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo("");
        assertThat(userDtoSaved).extractingJsonPathValue("$.email").isNull();
    }

    @Test
    void testDeserializeMissingFields() throws IOException {
        String json = "{\"id\":4}";
        UserDto deserializedUserDto = jacksonTester.parseObject(json);
        assertThat(deserializedUserDto.getId()).isEqualTo(4L);
        assertThat(deserializedUserDto.getName()).isNull();
        assertThat(deserializedUserDto.getEmail()).isNull();
    }

    @Test
    void testSerializeNullValues() throws Exception {
        UserDto userDto = UserDto.builder().id(null).name(null).email(null).build();
        JsonContent<UserDto> userDtoSaved = jacksonTester.write(userDto);

        assertThat(userDtoSaved).hasJsonPath("$.id");
        assertThat(userDtoSaved).hasJsonPath("$.name");
        assertThat(userDtoSaved).hasJsonPath("$.email");

        assertThat(userDtoSaved).extractingJsonPathValue("$.id").isNull();
        assertThat(userDtoSaved).extractingJsonPathValue("$.name").isNull();
        assertThat(userDtoSaved).extractingJsonPathValue("$.email").isNull();
    }

    @Test
    void testDeserializeEmptyJson() throws IOException {
        String json = "{}";

        UserDto deserializedUserDto = jacksonTester.parseObject(json);

        assertThat(deserializedUserDto.getId()).isNull();
        assertThat(deserializedUserDto.getName()).isNull();
        assertThat(deserializedUserDto.getEmail()).isNull();
    }

    @Test
    void testDeserializeInvalidEmail() throws IOException {
        String json = "{\"id\":1,\"name\":\"Test\",\"email\":\"invalid-email\"}";

        UserDto deserializedUserDto = jacksonTester.parseObject(json);

        assertThat(deserializedUserDto.getId()).isEqualTo(1L);
        assertThat(deserializedUserDto.getName()).isEqualTo("Test");
        assertThat(deserializedUserDto.getEmail()).isEqualTo("invalid-email");
    }
}
