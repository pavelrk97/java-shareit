package ru.practicum.shareit.user;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(userDto1.getName());
        assertThat(createdUser.getEmail()).isEqualTo(userDto1.getEmail());
    }

    @Test
    void updateUser() {
        UserDto createdUser = userService.create(userDto1);
        UserDto userUpdateDto = new UserDto(null, "UpdatedName", "updated@yandex.ru");
        UserDto updatedUser = userService.update(createdUser.getId(), userUpdateDto);

        assertThat(updatedUser.getName()).isEqualTo("UpdatedName");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@yandex.ru");
    }

    @Test
    void updateWhenEmailIsDuplicated() {
        userService.create(userDto1);
        userService.create(userDto2);
        UserDto userUpdateDto = new UserDto(null, "UpdatedName", "test.@yandex.ru");

        assertThatThrownBy(() -> userService.update(userDto2.getId(), userUpdateDto))
                .isInstanceOf(DuplicatedDataException.class);
    }

    @Test
    void findUserByIdReturnUser() {
        UserDto createdUser = userService.create(userDto1);
        UserDto foundUser = userService.findUserById(createdUser.getId());

        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getName()).isEqualTo(createdUser.getName());
        assertThat(foundUser.getEmail()).isEqualTo(createdUser.getEmail());
    }

    @Test
    void findUserByIdWhenUserNotFound() {
        assertThatThrownBy(() -> userService.findUserById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteUser() {
        UserDto createdUser = userService.create(userDto1);
        userService.delete(createdUser.getId());

        assertThatThrownBy(() -> userService.findUserById(createdUser.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAllReturnAllUsers() {
        userService.create(userDto1);
        userService.create(userDto2);
        List<UserDto> users = userService.findAll();

        assertThat(users).hasSize(2);
    }
}
