package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    @Test
    void toUserFullReturnsMappedObjectTest() {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();
        User mappedUser = UserMapper.toUser(userDto);

        assertThat(mappedUser.getId()).isEqualTo(userDto.getId());
        assertThat(mappedUser.getName()).isEqualTo(userDto.getName());
        assertThat(mappedUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void toUserReturnsMappedObjectWithNullFieldsTest() {
        UserDto userDto = UserDto.builder().id(null).name("").email(null).build();
        User mappedUser = UserMapper.toUser(userDto);

        assertThat(mappedUser.getId()).isNull();
        assertThat(mappedUser.getName()).isEqualTo("");
        assertThat(mappedUser.getEmail()).isNull();
    }

    @Test
    void toUserReturnsMappedObjectWithPartialDataTest() {
        UserDto userDto = UserDto.builder()
                .id(1L).name("Test").email(null).build();
        User mappedUser = UserMapper.toUser(userDto);

        assertThat(mappedUser.getId()).isEqualTo(userDto.getId());
        assertThat(mappedUser.getName()).isEqualTo(userDto.getName());
        assertThat(mappedUser.getEmail()).isNull();
    }

    @Test
    void toUserValidUserDtoReturnsCorrectUser() {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();
        User user = UserMapper.toUser(userDto);

        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void toUserNullUserDtoReturnsUserWithNullFields() {
        UserDto userDto = UserDto.builder().id(null).name(null).email(null).build();
        User user = UserMapper.toUser(userDto);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
    }

    @Test
    void toUserEmptyNameReturnsUserWithEmptyName() {
        UserDto userDto = UserDto.builder().id(1L).name("").email("test@yandex.ru").build();
        User user = UserMapper.toUser(userDto);

        assertThat(user.getName()).isEqualTo("");
    }
}
