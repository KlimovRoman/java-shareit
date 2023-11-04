package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select i from Booking i " +
            "where i.booker = ?1")
    List<Booking> findByBooker_Id(User booker);

    @Query("select i from Booking i " +
            "where i.item = ?1")
    List<Booking> findByItem_Id(Item item);

    @Query("select i from Booking i " +
            "where i.item = ?1 AND i.status = ?2")
    List<Booking> findByItem_IdAndStatus(Item item, BookingStatus status);

    @Query("select i from Booking i " +
            "where i.booker = ?1 AND i.status = ?2")
    List<Booking> findByBooker_IdAndStatus(User booker, BookingStatus status);

    @Query("select i from Booking i " +
            "where i.booker = ?1 AND i.item = ?2")
    List<Booking> findByBooker_IdAndItem(User booker, Item item);
}
