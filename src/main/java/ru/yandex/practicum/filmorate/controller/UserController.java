package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int nextId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        log.info("Добавлен новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    private int getNextId() {
        return nextId++;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        @NonNull
        User user = users.get(newUser.getId());
        user.setEmail(newUser.getEmail());
        user.setLogin(newUser.getLogin());
        user.setName(newUser.getName());
        user.setBirthday(newUser.getBirthday());
        log.info("User updated: {}", user);
        users.put(newUser.getId(), user);
        return user;
    }
}
