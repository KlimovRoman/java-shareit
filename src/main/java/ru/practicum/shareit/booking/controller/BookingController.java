package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody BookingCreateDto bookingCreateDto) {
       return bookingService.createBooking(userId, bookingCreateDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int bookingId, @RequestParam Boolean approved) {

        return bookingService.approveBooking(userId,bookingId,approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(defaultValue = "ALL") String state) {
        BookingState bookingState = BookingState.valueOf(state);
        return bookingService.getAllBookingsByUser(userId,bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByItemsOwner(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(defaultValue = "ALL") String state) {
        BookingState bookingState = BookingState.valueOf(state);
        return bookingService.getAllBookingsByItemsOwner(userId, bookingState);
    }
}
