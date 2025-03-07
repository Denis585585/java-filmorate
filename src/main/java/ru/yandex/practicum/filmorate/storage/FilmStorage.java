package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer filmId);

    Collection<Film> getFilms();

    Film getFilmById(Integer filmId);

    Collection<Film> getPopularFilms(Integer count);
}
