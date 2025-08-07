package shareit.user;

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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().name("Test").email("test@yandex.ru").build();
        user = UserMapper.toUser(userDto);
        user.setId(1L);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto createdUserDto = userService.create(userDto);
        assertEquals(userDto.getName(), createdUserDto.getName());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto foundUserDto = userService.findUserById(1L);
        assertEquals(userDto.getName(), foundUserDto.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUserReturnUserDto() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto createdUserDto = userService.create(userDto);
        assertEquals(userDto.getName(), createdUserDto.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto userUpdateDto = UserDto.builder().name("Update").email("update@yandex.ru").build();
        UserDto updatedUserDto = userService.update(1L, userUpdateDto);
        assertEquals("Update", updatedUserDto.getName());
        assertEquals("update@yandex.ru", updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserIfThrowNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(1L, userDto));
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findUserByIdWhenUserFound() {
        Long userId = 1L;
        User expectedUser = User.builder().id(1L).name("Test").email("test@yandex.ru").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserDto expectedUserDto = UserMapper.toUserDto(expectedUser);

        UserDto actualUserDto = userService.findUserById(userId);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findUserByIdWhenUserNotFound() {
        Long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException userNotFoundException = assertThrows(NotFoundException.class,
                () -> userService.findUserById(userId));

        assertEquals(userNotFoundException.getMessage(), "Пользователь с id " + userId + " не найден");
    }

    @Test
    void findAllUsersTest() {
        List<User> expectedUsers = List.of(new User());
        List<UserDto> expectedUserDto = expectedUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUsersDto = userService.findAll();

        assertEquals(actualUsersDto.size(), 1);
        assertEquals(actualUsersDto, expectedUserDto);
    }

    @Test
    void updateUserWithDuplicateEmail() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("duplicate@yandex.ru")).thenReturn(true);
        UserDto userUpdateDto = UserDto.builder().email("duplicate@yandex.ru").build();

        assertThrows(DuplicatedDataException.class, () -> userService.update(1L, userUpdateDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserWithSameEmail() {
        User user = User.builder().id(1L).name("Test User").email("test2@yandex.ru").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userUpdateDto = UserDto.builder().email("test@yandex.ru").build();
        User updatedUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(userUpdateDto.getEmail())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto updatedUserDto = userService.update(1L, userUpdateDto);

        assertEquals("test@yandex.ru", updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserWhenEmailIsTheSameThenUpdateSuccessfully() {
        Long userId = 1L;
        String existingEmail = "test@yandex.ru";
        UserDto userUpdateDto = UserDto.builder().email(existingEmail).build();
        User existingUser = User.builder().id(userId).name("Old Name").email(existingEmail).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        UserDto updatedUserDto = userService.update(userId, userUpdateDto);

        assertEquals(existingEmail, updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findAllUsersReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> actualUsersDto = userService.findAll();

        assertTrue(actualUsersDto.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteNonExistingUser() {
        Long userId = 999L;
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void updateUserWithEmptyName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto userUpdateDto = UserDto.builder().name("").email("update@yandex.ru").build();
        UserDto updatedUserDto = userService.update(1L, userUpdateDto);

        assertEquals("", updatedUserDto.getName());
        assertEquals("update@yandex.ru", updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserWithEmptyEmail() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userUpdateDto = UserDto.builder().name("Update").email("").build();
        UserDto updatedUserDto = userService.update(1L, userUpdateDto);

        assertEquals("Update", updatedUserDto.getName());
        assertEquals("", updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserWhenValidInputThenUserCreated() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto createdUserDto = userService.create(userDto);
        assertEquals(userDto.getName(), createdUserDto.getName());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findUserByIdWhenUserExistsThenReturnUserDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto foundUserDto = userService.findUserById(1L);
        assertEquals(userDto.getName(), foundUserDto.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserByIdWhenUserDoesNotExistThenThrowNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    void updateUserWhenUserExistsAndEmailNotDuplicateThenUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("update@yandex.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto userUpdateDto = UserDto.builder().name("Update").email("update@yandex.ru").build();
        UserDto updatedUserDto = userService.update(1L, userUpdateDto);
        assertEquals("Update", updatedUserDto.getName());
        assertEquals("update@yandex.ru", updatedUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserWhenUserDoesNotExistThenThrowNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(1L, userDto));
    }

    @Test
    void updateUserWhenDuplicateEmailThenThrowDuplicatedDataException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("duplicate@yandex.ru")).thenReturn(true);
        UserDto userUpdateDto = UserDto.builder().email("duplicate@yandex.ru").build();
        assertThrows(DuplicatedDataException.class, () -> userService.update(1L, userUpdateDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserWhenUserExistsThenDeleteUser() {
        Long userId = 1L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findAllUsersWhenUsersExistThenReturnListOfUserDtos() {
        List<User> expectedUsers = List.of(user);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUsersDto = userService.findAll();

        assertEquals(1, actualUsersDto.size());
        assertEquals(UserMapper.toUserDto(user), actualUsersDto.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findAllUsersWhenNoUsersExistThenReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDto> actualUsersDto = userService.findAll();
        assertTrue(actualUsersDto.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}