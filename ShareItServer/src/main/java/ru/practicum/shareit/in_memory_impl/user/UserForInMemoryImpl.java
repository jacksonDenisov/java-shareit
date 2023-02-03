package ru.practicum.shareit.in_memory_impl.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class UserForInMemoryImpl {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "E-mail пользователя не прошел валидацию.")
    private String email;
}
