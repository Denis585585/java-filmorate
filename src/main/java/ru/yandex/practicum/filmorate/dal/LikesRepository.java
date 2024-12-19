package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@Repository
public class LikesRepository extends BaseRepository<Film> {

    private static final String INSERT_QUERY_FOR_FILM = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_QUERY_OF_FILM = "DELETE FROM films_likes WHERE fikm_id = ? AND user_id = ?";

    public LikesRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, Class<Film> entityType) {
        super(jdbc, mapper, entityType);
    }

    public void addLike(Long filmId, Long userId) {
        update(INSERT_QUERY_FOR_FILM, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        update(DELETE_QUERY_OF_FILM, filmId, userId);
    }
}
