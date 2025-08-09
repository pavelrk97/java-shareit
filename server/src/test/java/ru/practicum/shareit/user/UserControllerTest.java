package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser() throws Exception {
        UserDto userDto = UserDto.builder().name("Test").email("test@yandex.ru").build();
        UserDto createdUserDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();

        when(userService.create(any(UserDto.class))).thenReturn(createdUserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void updateUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = UserDto.builder().name("Updated").email("updated@yandex.ru").build();
        UserDto updatedUserDto = UserDto.builder().id(userId).name("Updated").email("updated@yandex.ru").build();

        when(userService.update(eq(userId), any(UserDto.class))).thenReturn(updatedUserDto);

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));

        verify(userService, times(1)).update(eq(userId), any(UserDto.class));
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 1L;
        UserDto userDto = UserDto.builder().id(userId).name("Test").email("test@yandex.ru").build();

        when(userService.findUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));

        verify(userService, times(1)).findUserById(userId);
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    void getAllUsers() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).name("Test").email("test@yandex.ru").build();
        List<UserDto> userDtoList = Collections.singletonList(userDto);

        when(userService.findAll()).thenReturn(userDtoList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(userService, times(1)).findAll();
    }

    @Test
    void getAllUsers_emptyList() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1)).findAll();
    }
}
