package ru.practicum.shareit.booking.service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    public BookingDto createBooking (int userId, BookingCreateDto bookingDto);

    public BookingDto approveBooking (int userId,int bookingId,boolean approved);

    public BookingDto getBookingById (int userId,int bookingId);

    public List<BookingDto> getAllBookingsByUser(int userId, BookingState state);

    public List<BookingDto>  getAllBookingsByItemsOwner (int userId, BookingState state);
}