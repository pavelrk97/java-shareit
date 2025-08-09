package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {

    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;

    @Test
    void createItemRequestReturnSavedItemRequest() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@yandex.ru");
        User savedUser = userRepository.save(user);

        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("Test item");

        ItemRequestDto createdItemRequest = itemRequestService.create(savedUser.getId(), itemRequestCreateDto);

        assertNotNull(createdItemRequest.getId());
        assertEquals(itemRequestCreateDto.getDescription(), createdItemRequest.getDescription());
    }

    @Test
    void getUserRequestsReturnListOfUserRequests() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@yandex.ru");
        User savedUser = userRepository.save(user);

        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("Test item");
        itemRequestService.create(savedUser.getId(), itemRequestCreateDto);

        List<ItemRequestDto> userRequests = itemRequestService.getUserRequests(savedUser.getId());

        assertEquals(1, userRequests.size());
        assertEquals(itemRequestCreateDto.getDescription(), userRequests.get(0).getDescription());
    }
}