package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_QUERY = "INSERT INTO films(film_name, description," +
            " release_date, duration, mpa_id)" + "VALUES (?, ?, ?, ?, ?)";
    private static final String QUERY_FIND_FILM_BY_ID = "SELECT * FROM films f, mpa_ratings m " +
            "WHERE f.mpa_id = m.mpa_id AND f.film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET film_name = ?, description = ?," +
            " release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String QUERY_FOR_ALL_FILMS = "SELECT * FROM films f," +
            "mpa_ratings m WHERE f.mpa_id = m.mpa_id";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String QUERY_POPULAR_FILMS = "SELECT * FROM films f LEFT JOIN mpa_ratings m " +
            "ON f.mpa_id = m.mpa_id LEFT JOIN (SELECT film_id, COUNT(film_id) AS LIKES FROM films_likes " +
            "GROUP BY film_id) fl ON f.film_id = fl.film_id ORDER BY LIKES DESC LIMIT ?";
    private static final String QUERY_ALL_GENRES_FILMS = "SELECT * FROM films_genres fg, " +
            "genres g WHERE fg.genre_id = g.genre_id";
    private static final String QUERY_GENRES_BY_FILM = "SELECT * FROM genres g, film_genres fg " +
            "WHERE g.genre_id = fg.genre_id AND fg.film_id = ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, Class<Film> entityType) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Collection<Film> getFilms() {
        Collection<Film> films = findMany(QUERY_FOR_ALL_FILMS);
        Map<Long, Set<Genre>> genres = getAllGenres();
        for (Film film : films) {
            film.setGenres(genres.getOrDefault(film.getId(), Collections.emptySet()));
        }
        return films;
    }


    @Override
    public Film addFilm(Film film) {
        Long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        Film film = findOne(QUERY_FIND_FILM_BY_ID, filmId);
        film.setGenres(getGenresByFilm(filmId));
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) {
        Collection<Film> films = findMany(QUERY_POPULAR_FILMS, count);
        Map<Long, Set<Genre>> genres = getAllGenres();
        for (Film film : films) {
            if (genres.containsKey(film.getId())) {
                film.setGenres(genres.get(film.getId()));
            }
        }
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public void deleteFilm(Long filmId) {
        delete(DELETE_QUERY, filmId);
    }

    private Map<Long, Set<Genre>> getAllGenres() {
        Map<Long, Set<Genre>> genres = new HashMap<>();
        return jdbc.query(QUERY_ALL_GENRES_FILMS, (ResultSet rs) -> {
            while (rs.next()) {
                Long filmId = rs.getLong("film_id");
                Long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                genres.computeIfAbsent(filmId, k -> new HashSet<>()).add(new Genre(genreId, genreName));
            }
            return genres;
        });
    }

    private Set<Genre> getGenresByFilm(Long filmId) {
        return jdbc.query(QUERY_GENRES_BY_FILM, (ResultSet rs) -> {
            Set<Genre> genres = new HashSet<>();
            while (rs.next()) {
                Long genreId = rs.getLong("GENRE_ID");
                String genreName = rs.getString("GENRE_NAME");
                genres.add(new Genre(genreId, genreName));
            }
            return genres;
        }, filmId);
    }
}
