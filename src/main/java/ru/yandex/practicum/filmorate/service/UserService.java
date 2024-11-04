package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userId.equals(friendId)) {
            log.error("Error, u don't add myself");
            throw new ValidationException("Error, u don't add myself");
        }
        if (user.getFriends().contains(friendId)) {
            log.info("User with this id {} also your friend", friendId);
            throw new ValidationException("User with this " + friendId + "also your friend");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("User with id {} deleted", friendId);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Long> commonFriends = new HashSet<>(user.getFriends());
        if (commonFriends.retainAll(friend.getFriends())) {
            return commonFriends.stream()
                    .map(userStorage::getUserById)
                    .toList();
        } else {
            throw new NotFoundException("You don't have mutual friends");
        }
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .toList();
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }
}
