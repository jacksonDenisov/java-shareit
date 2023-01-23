package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter @Setter @ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private long ownerId;

    @Column(name = "request_id")
    private long requestId;
}
