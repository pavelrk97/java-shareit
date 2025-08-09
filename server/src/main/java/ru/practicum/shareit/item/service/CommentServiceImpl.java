package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private static final String USER_NOT_FOUND = " не найден.";
    private static final String USER_ID = "Пользователь с ID ";

    @Override
    public CommentDto createComment(Long userId, CommentCreateDto commentCreateDto, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_ID + userId + USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена."));

        if (bookingRepository.findAllByUserBookings(userId, itemId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("У пользователя с id " + userId + " должно быть хотя бы одно завершенное бронирование предмета с id " + itemId + " для возможности оставить комментарий.");
        }

        Comment comment = CommentMapper.toComment(commentCreateDto, item, user);
        Comment savedComment = commentRepository.save(comment);
        log.info("Создан комментарий с ID: {} для item ID: {}", savedComment.getId(), itemId);

        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getItemComments(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        log.info("Найдено {} комментариев для item ID: {}", comments.size(), itemId);
        return comments.stream()
                .filter(Objects::nonNull)
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
