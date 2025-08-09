package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, CommentCreateDto commentCreateDto, Long itemId);
    List<CommentDto> getItemComments(Long itemId);
}

