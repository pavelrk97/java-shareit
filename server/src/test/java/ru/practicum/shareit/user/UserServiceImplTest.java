package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "test@yandex.ru";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = createUserDto("Test", EMAIL);
        user = createUser(USER_ID, "Test", EMAIL);
    }

    @Test
    void createUser_ShouldReturnCreatedUserDto() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUserDto = userService.create(userDto);

        assertEquals(userDto.getName(), createdUserDto.getName());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findUserById_WhenUserExists_ShouldReturnUserDto() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserDto foundUserDto = userService.findUserById(USER_ID);

        assertEquals(userDto.getName(), foundUserDto.getName());
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findUserById_WhenUserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(USER_ID));
    }

    @Test
    void updateUser_WhenUserExistsAndEmailNotDuplicate_ShouldUpdateUser() {
        UserDto updateDto = createUserDto("Update", "update@yandex.ru");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUser = userService.update(USER_ID, updateDto);

        assertEquals(updateDto.getName(), updatedUser.getName());
        assertEquals(updateDto.getEmail(), updatedUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(USER_ID, userDto));
    }

    @Test
    void updateUser_WhenDuplicateEmail_ShouldThrowDuplicatedDataException() {
        String duplicateEmail = "duplicate@yandex.ru";
        UserDto updateDto = createUserDto(null, duplicateEmail);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(duplicateEmail)).thenReturn(true);

        assertThrows(DuplicatedDataException.class, () -> userService.update(USER_ID, updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDeleteById() {
        userService.delete(USER_ID);

        verify(userRepository).deleteById(USER_ID);
    }

    @Test
    void findAllUsers_WhenUsersExist_ShouldReturnListOfUserDtos() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals(UserMapper.toUserDto(user), users.get(0));
    }

    @Test
    void findAllUsers_WhenNoUsersExist_ShouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> users = userService.findAll();

        assertTrue(users.isEmpty());
    }

    // Вспомогательные методы для создания объектов
    private UserDto createUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

    private User createUser(Long id, String name, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
}
