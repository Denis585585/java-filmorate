package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikesRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikesRepository likesRepository;
    private final GenreRepository genreRepository;

    public FilmService(@Autowired @Qualifier("filmRepository") FilmStorage filmStorage,
                       @Autowired GenreRepository genreRepository,
                       @Autowired LikesRepository likesRepository) {
        this.filmStorage = filmStorage;
        this.likesRepository = likesRepository;
        this.genreRepository = genreRepository;
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getFilmById(filmId);
        likesRepository.addLike(filmId, userId);
        log.info("User {} liked film {}", userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.getFilmById(filmId);
        likesRepository.deleteLike(filmId, userId);
        log.info("User {} delete his like film {}", userId, filmId);
    }

    public Film geFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        Film createdFilm = filmStorage.addFilm(film);
        if (!createdFilm.getGenres().isEmpty()) {
            genreRepository.addGenre(createdFilm.getId(), createdFilm.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList());
        }
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundException("Film with this id not found");
        }
        Film updatedFilm = filmStorage.updateFilm(film);
        if (!updatedFilm.getGenres().isEmpty()) {
            genreRepository.deleteGenres(updatedFilm.getId());
            genreRepository.addGenre(updatedFilm.getId(), updatedFilm.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList());
        }
        return updatedFilm;
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
    }
}
