package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Repository
public class FriendshipRepository extends BaseRepository<User> {
    private static final String INSERT_QUERY = "INSERT INTO friends (user_id, fiend_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE friend_id = ? AND user_id = ?";
    private static final String QUERY_FOR_USER_FRIENDS = "SELECT * FROM users WHERE user_id IN" +
            "(SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String QUERY_FOR_COMMON_FRIENDS = "SELECT * FROM users WHERE user_id IN" +
            "(SELECT friend_id FROM friends WHERE user_id = ?) AND user_id IN " +
            "(SELECT friend_id FROM friend WHERE user_id = ?)";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<User> mapper, Class<User> entityType) {
        super(jdbc, mapper, entityType);
    }

    public void addFriend(Long userId, Long friendId) {
        update(INSERT_QUERY,
                userId,
                friendId
        );
    }

    public void deleteFriend(Long userId, Long friendId) {
        update(DELETE_QUERY,
                friendId,
                userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        return findMany(QUERY_FOR_COMMON_FRIENDS, userId, friendId);
    }

    public Collection<User> getAllFriends(Long userId) {
        return findMany(QUERY_FOR_USER_FRIENDS, userId);
    }

}
