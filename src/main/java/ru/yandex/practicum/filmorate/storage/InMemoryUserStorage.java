package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int nextId = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        log.info("Добавлен новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        return nextId++;
    }

    @Override
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

    @Override
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with this id not found");
        }
        users.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with this id not found");
        }
        return users.get(id);
    }
}
