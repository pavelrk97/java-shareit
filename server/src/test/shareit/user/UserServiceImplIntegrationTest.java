package shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto1 = new UserDto(1L, "Test", "test.@yandex.ru");
        userDto2 = new UserDto(2L, "Test2", "test2@yandex.ru");
    }

    @AfterEach
    void tearDown() {
        userService.findAll().forEach(userDto -> userService.delete(userDto.getId()));
    }

    @Test
    void createUser() {
        UserDto createdUser = userService.create(userDto1);
        assertNotNull(createdUser.getId());
        assertEquals(userDto1.getName(), createdUser.getName());
        assertEquals(userDto1.getEmail(), createdUser.getEmail());
    }

    @Test
    void updateUser() {
        UserDto createdUser = userService.create(userDto1);
        UserDto userUpdateDto = new UserDto(null, "UpdatedName", "updated@yandex.ru");
        UserDto updatedUser = userService.update(createdUser.getId(), userUpdateDto);

        assertEquals("UpdatedName", updatedUser.getName());
        assertEquals("updated@yandex.ru", updatedUser.getEmail());
    }

    @Test
    void updateWhenEmailIsDuplicated() {
        userService.create(userDto1);
        userService.create(userDto2);
        UserDto userUpdateDto = new UserDto(null, "UpdatedName", "test.@yandex.ru");
        assertThrows(DuplicatedDataException.class, () -> userService.update(userDto2.getId(), userUpdateDto));
    }

    @Test
    void findUserByIdReturnUser() {
        UserDto createdUser = userService.create(userDto1);
        UserDto foundUser = userService.findUserById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(createdUser.getName(), foundUser.getName());
        assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void findUserByIdWhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.findUserById(999L));
    }

    @Test
    void deleteUser() {
        UserDto createdUser = userService.create(userDto1);
        userService.delete(createdUser.getId());
        assertThrows(NotFoundException.class, () -> userService.findUserById(createdUser.getId()));
    }

    @Test
    void findAllReturnAllUsers() {
        userService.create(userDto1);
        userService.create(userDto2);
        List<UserDto> users = userService.findAll();
        assertEquals(2, users.size());
    }
}