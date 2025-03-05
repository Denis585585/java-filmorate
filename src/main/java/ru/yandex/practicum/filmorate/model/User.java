package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Login cannot contain spaces")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            return login;
        } else {
            return name;
        }
    }
}
