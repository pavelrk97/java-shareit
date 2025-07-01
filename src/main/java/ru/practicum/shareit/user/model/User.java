package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
