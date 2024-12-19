package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private final static String QUERY_FOR_ALL_GENRES = "SELECT * FROM genres";
    private final static String INSERT_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private final static String QUERY_FOR_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    private final static String DELETE_ALL = "DELETE FROM films_genres WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper, Class<Genre> entityType) {
        super(jdbc, mapper, entityType);
    }

    public Collection<Genre> getAllGenres() {
        return findMany(QUERY_FOR_ALL_GENRES);
    }

    public Genre getGenreById(Long id) {
        return findOne(QUERY_FOR_GENRE_BY_ID, id);
    }

    public void addGenre(Long filmId, List<Long> genresIds) {
        batchUpdateBase(INSERT_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genresIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return genresIds.size();
            }
        });
    }

    public void deleteGenres(Long filmId) {
        delete(DELETE_ALL, filmId);
    }
}
