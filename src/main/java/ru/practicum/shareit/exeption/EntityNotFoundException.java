package ru.practicum.shareit.exeption;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}