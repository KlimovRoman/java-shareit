package ru.practicum.shareit.item.dto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.List;

// а еще можно было бы использовать @UtilityClass
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto itemToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemDto itemToDtoComments(Item item,List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments

        );
    }

    public static ItemDto itemToDtoBooks(Item item, Booking lastBooking, Booking nextBooking,List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static Item dtoToItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user
        );
    }

    public static CommentDto commentToDto(Comment comment) {
        return new CommentDto(
          comment.getId(),
          comment.getText(),
          comment.getAuthor().getName(),
          comment.getCreated()
        );
    }
}