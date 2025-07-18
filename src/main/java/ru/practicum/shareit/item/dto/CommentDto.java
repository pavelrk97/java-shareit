package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Long id;
    Long itemId;
    String text;
    String authorName;
    LocalDateTime created;
}