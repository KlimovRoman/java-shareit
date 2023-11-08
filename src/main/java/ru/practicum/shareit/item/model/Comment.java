package ru.practicum.shareit.item.model;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //уникальный идентификатор коммента
    @NotBlank
    private String text; // текст
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item; // вещь к которой написан коммент

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author; //автор коммента
    private LocalDateTime created; //Дата создания коммента

    public Comment() {
    }

    public Comment(int id, String text, Item item, User author, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.item = item;
        this.created = created;
    }
}