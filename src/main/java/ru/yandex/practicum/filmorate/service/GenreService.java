package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Collection<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public Genre getGenreById(Long genreId) {
        return genreRepository.getGenreById(genreId);
    }

    public void updateGenre(Long filmId, List<Long> genresIds) {
        genreRepository.addGenre(filmId, genresIds);
    }

    public void deleteGenre(Long filmId) {
        genreRepository.deleteGenres(filmId);
    }
}
