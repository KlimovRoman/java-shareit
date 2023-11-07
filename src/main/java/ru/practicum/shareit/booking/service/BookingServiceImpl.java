package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(int userId, BookingCreateDto bookingCreateDto) {
        log.info("JPA - Добавление в БД booking");
        User booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!"));
        Item item = itemRepository.findById(bookingCreateDto.getItemId()).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        Booking bookingForSave = BookingMapper.createDtoToBooking(bookingCreateDto, item, booker);
        return BookingMapper.bookingToDto(bookingRepository.save(bookingForSave));
    }

    @Override
    public BookingDto approveBooking(int userId,int bookingId,boolean approved) {
        // надо обновить в бд бронирование присвоев ему новый статус
        log.info("JPA - Обновление статуса в БД booking");
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено!"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new EntityNotFoundException("Ошибка! Вещь может просматривать только автор!");
        } else if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.CANCELED);
        }
        return BookingMapper.bookingToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(int userId,int bookingId) {
        //user должен быть либо пользлвателем вещи либо создателем брони
        log.info("JPA - getBookingById ");
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено!"));
        if (booking.getItem().getOwner().getId() == userId  || booking.getBooker().getId() == userId) {
            return BookingMapper.bookingToDto(booking);
        } else {
            throw new EntityNotFoundException("Ошибка! Вещь может просматривать только автор!");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(int userId, BookingState state) {
        User booker = new User();
        List<BookingDto> booksDto = new ArrayList<>();
        List<Booking> books = new ArrayList<>();

        switch (state) {
            case ALL:
                log.info("JPA - getAllBookingsByUser / STATE = ALL ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_Id(booker);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                    return booksDto;
                } else {
                    return null;
                }

            case PAST:
                log.info("JPA - getAllBookingsByUser / STATE = PAST ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_Id(booker);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        if (book.getEnd().isBefore(LocalDateTime.now())) {
                            booksDto.add(BookingMapper.bookingToDto(book));
                        }
                    }
                    return booksDto;
                } else {
                    return null;
                }

            case FUTURE:
                log.info("JPA - getAllBookingsByUser / STATE = FUTURE ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_Id(booker);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        if (book.getStart().isAfter(LocalDateTime.now())) {
                            booksDto.add(BookingMapper.bookingToDto(book));
                        }
                    }
                    return booksDto;
                } else {
                    return null;
                }

            case CURRENT:
                log.info("JPA - getAllBookingsByUser / STATE = CURRENT ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_Id(booker);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        if (book.getStart().isBefore(LocalDateTime.now()) && book.getEnd().isAfter(LocalDateTime.now())) {
                            booksDto.add(BookingMapper.bookingToDto(book));
                        }
                    }
                    return booksDto;
                } else {
                    return null;
                }

            case WAITING:
                log.info("JPA - getAllBookingsByUser / STATE = WAITING ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_IdAndStatus(booker,BookingStatus.WAITING);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                    return booksDto;
                } else {
                    return null;
                }

            case REJECTED:
                log.info("JPA - getAllBookingsByUser / STATE = REJECTED ");
                booker = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найдено!"));
                books = bookingRepository.findByBooker_IdAndStatus(booker,BookingStatus.REJECTED);
                if (books.size() > 0) {
                    for (Booking book: books) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                    return booksDto;
                } else {
                    return null;
                }

            default: return null;
        }
    }

    @Override
    public List<BookingDto>  getAllBookingsByItemsOwner(int userId, BookingState state) {
        User owner = new User();
        List<BookingDto> booksDto = new ArrayList<>();
        List<Booking> books = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        owner = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено!"));
        items = itemRepository.findByUserId(owner);
        if (items.size() < 1) {
            return null;
        }

        switch (state) {
            case ALL:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = ALL ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_Id(item));
                }
                for (Booking book: books) {
                    booksDto.add(BookingMapper.bookingToDto(book));
                }
              return booksDto;
            case PAST:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = PAST ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_Id(item));
                }
                for (Booking book: books){
                    if (book.getEnd().isBefore(LocalDateTime.now())) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                }
                return booksDto;

            case FUTURE:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = FUTURE ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_Id(item));
                }
                for (Booking book: books) {
                    if (book.getEnd().isAfter(LocalDateTime.now())) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                }
                return booksDto;

            case CURRENT:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = CURRENT ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_Id(item));
                }
                for (Booking book: books) {
                    if (book.getStart().isBefore(LocalDateTime.now()) && book.getEnd().isAfter(LocalDateTime.now())) {
                        booksDto.add(BookingMapper.bookingToDto(book));
                    }
                }
                return booksDto;

            case WAITING:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = WAITING ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_IdAndStatus(item,BookingStatus.WAITING));
                }
                for (Booking book: books) {
                    booksDto.add(BookingMapper.bookingToDto(book));
                }
                return booksDto;

            case REJECTED:
                log.info("JPA - getAllBookingsByItemsOwner / STATE = REJECTED ");
                for (Item item: items) {
                    books.addAll(bookingRepository.findByItem_IdAndStatus(item,BookingStatus.REJECTED));
                }
                for (Booking book: books) {
                    booksDto.add(BookingMapper.bookingToDto(book));
                }
                return booksDto;

            default:
                return null;
        }

    }

}