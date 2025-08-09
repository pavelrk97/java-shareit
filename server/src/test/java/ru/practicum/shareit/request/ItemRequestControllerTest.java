package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utils.HeaderConstants;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;
    private ItemRequestCreateDto itemRequestCreateDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test")
                .created(LocalDateTime.now())
                .build();

        itemRequestCreateDto = ItemRequestCreateDto.builder()
                .description("Test")
                .build();
    }

    @Test
    void create() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).create(anyLong(), any());
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getAllRequestById(anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllRequestById(anyLong());
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllRequests(anyLong(), any(), any());
    }

    @Test
    void getUserRequests() throws Exception {
        when(itemRequestService.getUserRequests(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getUserRequests(anyLong());
    }

    @Test
    void getUserRequestsEmptyList() throws Exception {
        when(itemRequestService.getUserRequests(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(itemRequestService, times(1)).getUserRequests(anyLong());
    }

    @Test
    void getAllRequestsWithPagination() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all?from=0&size=10")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllRequests(anyLong(), any(), any());
    }

    @Test
    void getAllRequestsEmptyList() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header(HeaderConstants.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(itemRequestService, times(1)).getAllRequests(anyLong(), any(), any());
    }
}