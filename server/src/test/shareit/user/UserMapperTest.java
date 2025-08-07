package shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    @Test
    void toUserFullReturnsMappedObjectTest() {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();
        User mappedUser = UserMapper.toUser(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertEquals(mappedUser.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserReturnsMappedObjectWithNullFieldsTest() {
        UserDto userDto = UserDto.builder().id(null).name("").email(null).build();
        User mappedUser = UserMapper.toUser(userDto);

        assertNull(mappedUser.getId());
        assertEquals("", mappedUser.getName());
        assertNull(mappedUser.getEmail());
    }

    @Test
    void toUserReturnsMappedObjectWithPartialDataTest() {
        UserDto userDto = UserDto.builder()
                .id(1L).name("Test").email(null).build();
        User mappedUser = UserMapper.toUser(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertNull(mappedUser.getEmail());
    }

    @Test
    void toUserValidUserDtoReturnsCorrectUser() {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();
        User user = UserMapper.toUser(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void toUserNullUserDtoReturnsUserWithNullFields() {
        UserDto userDto = UserDto.builder().id(null).name(null).email(null).build();
        User user = UserMapper.toUser(userDto);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void toUserEmptyNameReturnsUserWithEmptyName() {
        UserDto userDto = UserDto.builder().id(1L).name("").email("test@yandex.ru").build();
        User user = UserMapper.toUser(userDto);

        assertEquals("", user.getName());
    }
}