package ru.practicum.shareit.item.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int id;
    private String text;
    //private Item item;
    private String authorName;
    //private User author;
    private LocalDateTime created;

    public CommentDto() {

    }

    public CommentDto(int id, String text, String authorName,LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }

}