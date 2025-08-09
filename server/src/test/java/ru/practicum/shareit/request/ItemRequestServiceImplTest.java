package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void createWhenUserNotFound() {
        Long userId = 1L;
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.create(userId, itemRequestCreateDto));
    }

    @Test
    void createWhenUserFound() {
        Long userId = 1L;
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("description");
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setDescription("description");

        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        assertNotNull(itemRequestService.create(userId, itemRequestCreateDto));
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getUserRequestsWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getUserRequests(userId));
    }

    @Test
    void getAllRequests_shouldReturnRequestsExcludingUser() {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        User user = new User();
        user.setId(userId);

        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setRequester(new User());
        request1.getRequester().setId(2L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setRequester(user);

        List<ItemRequest> allItemRequests = List.of(request1, request2);

        Pageable pageable = PageRequest.of(0, size, Sort.by("created").descending());
        Page<ItemRequest> pageResult = new PageImpl<>(allItemRequests, pageable, allItemRequests.size());
        when(itemRequestRepository.findAll(pageable)).thenReturn(pageResult);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<ItemRequestDto> result = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getAllRequests_whenUserNotFound_thenThrowsException() {
        Long userId = 999L;
        int from = 0;
        int size = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getAllRequests(userId, from, size);
        });

        assertEquals("Пользователь с ID " + userId + " не найден.", notFoundException.getMessage());
    }

    @Test
    void getAllRequestsWhenUserNotFound() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequests(userId, from, size));
    }

    @Test
    void getAllRequestByIdWhenRequestNotFound() {
        Long requestId = 1L;
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequestById(requestId));
    }

    @Test
    void getAllRequestByIdWhenRequestFound() {
        Long requestId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(requestId)).thenReturn(Collections.emptyList());
        assertNotNull(itemRequestService.getAllRequestById(requestId));
    }

    @Test
    void getAllRequestsWhenFromIsNegativeThenThrowsIllegalArgumentException() {
        Long userId = 1L;
        Integer from = -1;
        Integer size = 10;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(NullPointerException.class, () -> itemRequestService.getAllRequests(userId, from, size));
    }

    @Test
    void getAllRequestsWhenSizeIsZeroThenThrowsIllegalArgumentException() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 0;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> itemRequestService.getAllRequests(userId, from, size));
    }

    @Test
    void getAllRequestsWhenSizeIsNegativeThenThrowsIllegalArgumentException() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = -1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> itemRequestService.getAllRequests(userId, from, size));
    }

    @Test
    void getAllRequestsWhenFromAndSizeAreValidThenReturnsRequests() {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        User user = new User();
        user.setId(userId);

        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setRequester(new User());
        request1.getRequester().setId(2L);

        List<ItemRequest> allItemRequests = List.of(request1);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        Page<ItemRequest> pageResult = new PageImpl<>(allItemRequests, pageable, allItemRequests.size());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAll(pageable)).thenReturn(pageResult);

        List<ItemRequestDto> result = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}