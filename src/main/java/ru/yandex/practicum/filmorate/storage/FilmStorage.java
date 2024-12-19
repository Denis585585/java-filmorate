package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long filmId);

    Collection<Film> getFilms();

    Film getFilmById(Long filmId);

    Collection<Film> getPopularFilms(Long count);
}
