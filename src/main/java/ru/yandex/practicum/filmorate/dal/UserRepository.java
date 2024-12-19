package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String QUERY_FOR_ALL_USER = "SELECT * FROM users";
    private static final String QUERY_FOR_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, username, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? " +
            "WHERE user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper, Class<User> entityType) {
        super(jdbc, mapper, entityType);
    }


    @Override
    public User createUser(User user) {
        Long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public Collection<User> getUsers() {
        return findMany(QUERY_FOR_ALL_USER);
    }

    @Override
    public User getUserById(Long id) {
        return findOne(QUERY_FOR_USER_BY_ID, id);
    }
}
