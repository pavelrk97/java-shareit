package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    Long id;
    String name;
    @Email
    String email;
}
