package ru.practicum.shareit.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    private int itemId;
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;

    public BookingCreateDto (int itemId, int id, LocalDateTime start, LocalDateTime end ) {
        this.itemId = itemId;
        this.id = id;
        this.start = start;
        this.end = end;
    }
    public BookingCreateDto () {

    }
}
