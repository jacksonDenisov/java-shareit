package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "E-mail пользователя не прошел валидацию.")
    private String email;
}