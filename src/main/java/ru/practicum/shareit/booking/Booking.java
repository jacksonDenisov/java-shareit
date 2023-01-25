package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.Item;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date_time")
    private LocalDateTime start;

    @Column(name = "end_date_time")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "booker_id")
    private Long bookerId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
