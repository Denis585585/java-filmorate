package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        filmStorage.getFilmById(filmId)
                .getLikes()
                .add(user.getId());
        log.info("User {} liked film {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        filmStorage.getFilmById(filmId)
                .getLikes()
                .remove(user.getId());
        log.info("User {} delete his like film {}", userId, filmId);
    }

    public Film geFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        return filmStorage.getFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .toList();
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Long filmId) {
        filmStorage.deleteFilm(filmId);
    }
}
