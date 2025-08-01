package ru.practicum.shareit.booking.dto;

import jakarta.validation.ValidationException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State parseState(String stateString) {
        try {
            return State.valueOf(stateString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный state: " + stateString);
        }
    }
}