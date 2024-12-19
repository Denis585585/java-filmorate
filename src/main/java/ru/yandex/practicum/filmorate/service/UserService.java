package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipRepository friendshipRepository;

    public UserService(@Autowired @Qualifier("userRepository") UserStorage userStorage,
                       @Autowired FriendshipRepository friendshipRepository) {
        this.userStorage = userStorage;
        this.friendshipRepository = friendshipRepository;
    }

    public void addFriend(Long userId, Long friendId) {
        friendshipRepository.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendshipRepository.deleteFriend(userId, friendId);
        log.info("User with id {} deleted", friendId);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        return friendshipRepository.getCommonFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        return friendshipRepository.getAllFriends(userId);
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
