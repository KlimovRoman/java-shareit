package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserDao userDao; //внедрили репозиторий юзеров
    private final ItemDao itemDao; //внедрили репозиторий вещей
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final ItemRepository itemRepo;
    private final CommentRepository commentRepo;

    @Override
    public ItemDto addItem(int userId, ItemDto itemDtoToAdd) {
        log.info("JPA - Добавление в БД Item");
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!"));
        return ItemMapper.itemToDto(itemRepo.save(ItemMapper.dtoToItem(itemDtoToAdd, user)));
    }

    @Override
    public ItemDto updItem(int userId, int itemId, ItemDto itemDtoToAdd) {
        log.info("JPA - Обновление в БД Item");
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден!"));
        if (userId !=  item.getOwner().getId()) {
            throw new EntityNotFoundException("Ошибка! Вещь может просматривать только автор!");
        }
        if(itemDtoToAdd.getName() != null && !itemDtoToAdd.getName().isBlank()) {
            item.setName(itemDtoToAdd.getName());
        }
        if(itemDtoToAdd.getDescription() != null && !itemDtoToAdd.getDescription().isBlank()) {
            item.setDescription(itemDtoToAdd.getDescription());
        }
        if(itemDtoToAdd.getAvailable() != null ) {
            item.setAvailable(itemDtoToAdd.getAvailable());
        }

        return ItemMapper.itemToDto(itemRepo.save(item));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        log.info("JPA - getItemById");
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        List<CommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentRepo.findByItem_Id(item);
        for (Comment comment: comments) {
            commentsDto.add(ItemMapper.commentToDto(comment));
        }
        return ItemMapper.itemToDtoComments(item,commentsDto);
    }

    @Override
    public List<ItemDto> getItemsByUser(int userId) {
        log.info("JPA - getItemsByUser id = {}",userId);

        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден!"));
        List<Item> items = new ArrayList<>(itemRepo.findByUserId(user));
        List<ItemDto> dtoItems = new ArrayList<>(); // итогоый список ДТОшек для ретерна
        List<Booking> booksForItem = new ArrayList<>(); // тут хрантся все брони для конкретной вещи
        List<Comment> comments = new ArrayList<>();
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Item item: items){
            booksForItem =  bookingRepo.findByItem_Id(item);
            comments = commentRepo.findByItem_Id(item);
            for (Comment comment: comments) {
                commentsDto.add(ItemMapper.commentToDto(comment));
            }
            Booking lastBooking = new Booking();
            Booking nextBooking = new Booking();
            lastBooking.setEnd(LocalDateTime.now());
            nextBooking.setStart(LocalDateTime.now().plusDays(365));

            for (Booking book: booksForItem){
                if (book.getEnd().isBefore(LocalDateTime.now())) {
                    if (book.getEnd().isAfter(lastBooking.getEnd())){
                        lastBooking = book; // находим максимальную дату конца до текущего дня, она и будет датой последнего
                    }
                } else if (book.getStart().isAfter(LocalDateTime.now())) {
                    if (book.getStart().isBefore(nextBooking.getStart())) {
                        nextBooking = book; //находим минимальную дату старта после текущего дня, она и будт датой ближайшего
                    }
                }
            }

            dtoItems.add(ItemMapper.itemToDtoBooks(item,lastBooking,nextBooking,commentsDto));
        }
        return dtoItems;
    }

    @Override
    public CommentDto addComment(int itemId, int userId, Comment comment) {
        log.info("JPA - Добавление в БД Comment");
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        User booker = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден!"));
        List<Booking> bookListForCheck = bookingRepo.findByBooker_IdAndItem(booker, item);
        String a = String.valueOf(bookListForCheck.size());
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());

        if (bookListForCheck.size() > 0) {
            Booking booking = bookListForCheck.get(0);
            if (booking.getEnd().isBefore(LocalDateTime.now())) {
                return ItemMapper.commentToDto(commentRepo.save(comment));
            } else {
                throw new ValidationException("Ошибка! Коммент можно оставлять только после завершения аренды!");
            }

        } else {
            throw new EntityNotFoundException("Ошибка! Коммент  может оставлять только букер!");
        }
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        log.info("JPA - searchItems");
        if (text.isBlank()) {
            return  List.of();
        } else {
            text =  text.toLowerCase();
            List<ItemDto> dtoItems = new ArrayList<>();
            List<Item> items = new ArrayList<>(itemRepo.search(text));
            for (Item itm: items) {
                dtoItems.add(ItemMapper.itemToDto(itm));
            }
            return dtoItems;
        }
    }
}
